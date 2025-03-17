package com.gongcha.berrymatch.springSecurity.service;

import com.gongcha.berrymatch.exception.BusinessException;
import com.gongcha.berrymatch.exception.ErrorCode;
import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import com.gongcha.berrymatch.springSecurity.constants.TokenStatus;
import com.gongcha.berrymatch.springSecurity.domain.Token;
import com.gongcha.berrymatch.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;

import static com.gongcha.berrymatch.exception.ErrorCode.*;
import static com.gongcha.berrymatch.exception.ErrorCode.*;
import static com.gongcha.berrymatch.springSecurity.constants.JwtRule.*;

@Service
@Transactional(readOnly = true)
@Slf4j
public class JwtFacadeService implements JwtFacade {
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenService tokenService;
    private final JwtGenerator jwtGenerator;
    private final JwtUtil jwtUtil;

    private final Key ACCESS_SECRET_KEY;
    private final Key REFRESH_SECRET_KEY;
    private final long ACCESS_EXPIRATION;
    private final long REFRESH_EXPIRATION;

    public JwtFacadeService(CustomUserDetailsService customUserDetailsService,
                            TokenService tokenService,
                            JwtGenerator jwtGenerator, JwtUtil jwtUtil,
                            @Value("${BERRYMATCH_ACCESS_TOKEN_SECRETKEY}") String ACCESS_SECRET_KEY,
                            @Value("${BERRYMATCH_REFRESH_TOKEN_SECRETKEY}") String REFRESH_SECRET_KEY,
                            @Value("${BERRYMATCH_ACCESS_TOKEN_EXPIRATION_TIME}") long ACCESS_EXPIRATION,
                            @Value("${BERRYMATCH_REFRESH_TOKEN_EXPIRATION_TIME}") long REFRESH_EXPIRATION) {
        this.customUserDetailsService = customUserDetailsService;
        this.tokenService = tokenService;
        this.jwtGenerator = jwtGenerator;
        this.jwtUtil = jwtUtil;
        this.ACCESS_SECRET_KEY = jwtUtil.getSigningKey(ACCESS_SECRET_KEY);
        this.REFRESH_SECRET_KEY = jwtUtil.getSigningKey(REFRESH_SECRET_KEY);
        this.ACCESS_EXPIRATION = ACCESS_EXPIRATION;
        this.REFRESH_EXPIRATION = REFRESH_EXPIRATION;
    }


    @Override
    public String generateAccessToken(HttpServletResponse response, User requestUser) {
        String accessToken = jwtGenerator.generateAccessToken(ACCESS_SECRET_KEY, ACCESS_EXPIRATION, requestUser);
        String bearer = ACCESS_PREFIX.getValue() + accessToken;
        response.setHeader(ACCESS_HEADER.getValue(), bearer);
        response.setHeader(ACCESS_REISSUED_HEADER.getValue(), "False");

        return accessToken;
    }

    @Override
    @Transactional
    public String generateRefreshToken(HttpServletResponse response, User requestUser) {
        String refreshToken = jwtGenerator.generateRefreshToken(REFRESH_SECRET_KEY, REFRESH_EXPIRATION, requestUser);
        ResponseCookie cookie = setTokenToCookie(REFRESH_PREFIX.getValue(), refreshToken, REFRESH_EXPIRATION / 1000);
        response.addHeader(REFRESH_ISSUE.getValue(), cookie.toString());

        tokenService.save(new Token(requestUser.getIdentifier(), refreshToken, requestUser.getProviderInfo()));
        return refreshToken;
    }

    private ResponseCookie setTokenToCookie(String tokenPrefix, String token, long maxAgeSeconds) {
        return ResponseCookie.from(tokenPrefix, token)
                .path("/")
                .maxAge(maxAgeSeconds)
                .httpOnly(true)
                .sameSite("Strict")
                .secure(true)
                .build();
    }

    @Override
    public boolean validateAccessToken(String token) {
        return jwtUtil.getTokenStatus(token, ACCESS_SECRET_KEY) == TokenStatus.AUTHENTICATED;
    }

    @Override
    public boolean validateRefreshToken(String token, String identifier, ProviderInfo providerInfo) {


        boolean isRefreshValid = jwtUtil.getTokenStatus(token, REFRESH_SECRET_KEY) == TokenStatus.AUTHENTICATED;

        boolean isHijacked = tokenService.isRefreshHijacked(identifier, token, providerInfo);

        return isRefreshValid && !isHijacked;
    }

    @Override
    public void setReissuedHeader(HttpServletResponse response) {
        response.setHeader(ACCESS_REISSUED_HEADER.getValue(), "True");
    }

    @Override
    public String resolveAccessToken(HttpServletRequest request) {
        String bearerHeader = request.getHeader(ACCESS_HEADER.getValue());
        if (bearerHeader == null || bearerHeader.isEmpty()) {
            throw new BusinessException(JWT_NOT_FOUND_IN_HEADER);
        }
        System.out.println("엑세스 토큰 확인요");
        return bearerHeader.trim().substring(7);
    }

    @Override
    public String resolveRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new BusinessException(JWT_NOT_FOUND_IN_COOKIE);
        }
        String refreshToken = jwtUtil.resolveTokenFromCookie(cookies, REFRESH_PREFIX);

        if (refreshToken == null || refreshToken.isEmpty()) {
            System.out.println("리프레시 토큰이 없습니다.");
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUNT);
        }

        return refreshToken;

    }

    @Override
    public String getIdentifierFromRefresh(String refreshToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(REFRESH_SECRET_KEY)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            String identifier = claims.get("identifier", String.class);
            if (identifier == null) {
                identifier = claims.get("Identifier", String.class);
            }

            System.out.println(identifier);
            return identifier;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_JWT);
        }
    }

    @Override
    public ProviderInfo getProviderInfoFromRefresh(String refreshToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(REFRESH_SECRET_KEY)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            String providerInfo = claims.get("providerInfo", String.class);
            return ProviderInfo.valueOf(providerInfo.toUpperCase());
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_JWT);
        }
    }

    @Override
    public Authentication getAuthentication(String accessToken) {
        UserDetails principal = customUserDetailsService.loadUserByUsername(getUserPk(accessToken, ACCESS_SECRET_KEY));
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }

    private String getUserPk(String token, Key secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public String logout(HttpServletResponse response, String identifier, ProviderInfo providerInfo) {
        tokenService.deleteAllByIdentifierAndProviderInfo(identifier, providerInfo);
        tokenService.deleteAllByIdentifierAndProviderInfo(identifier, providerInfo);

        return "로그아웃 성공";
    }

    public ProviderInfo toProviderInfo(String key) {
        try {
            return ProviderInfo.valueOf(key.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("providerInfo가 적절한 형태가 아닙니다 : " + key);
        }
    }

    @Override
    public void deleteRefreshToken(String identifier, ProviderInfo providerInfo) {
        try {
            tokenService.deleteAllByIdentifierAndProviderInfo(identifier, providerInfo);
            tokenService.deleteAllByIdentifierAndProviderInfo(identifier, providerInfo);
        } catch (BusinessException e) {
            throw new BusinessException(ErrorCode.JWT_NOT_FOUND_IN_DB);
        }

    }

    @Override
    public boolean isRefreshTokenDuplicate(String identifier, ProviderInfo providerInfo) {
        return tokenService.isRefreshDuplicate(identifier, providerInfo);
    }



}
