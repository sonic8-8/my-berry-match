package com.gongcha.berrymatch.springSecurity.service;

import com.gongcha.berrymatch.exception.BusinessException;
import com.gongcha.berrymatch.springSecurity.constants.JwtRule;
import com.gongcha.berrymatch.springSecurity.constants.TokenStatus;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;

import static com.gongcha.berrymatch.exception.ErrorCode.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JwtUtil {

    /**
     * 토큰과 시크릿 키로 서명을 만들어본 후 유효한 토큰인지 확인해주는 메서드
     */
    public TokenStatus getTokenStatus(String token, Key secretKey) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return TokenStatus.AUTHENTICATED;
        } catch (ExpiredJwtException | IllegalArgumentException e) {
            log.error(INVALID_EXPIRED_JWT.getMessage());
            return TokenStatus.EXPIRED;
        } catch (JwtException e) {
            throw new BusinessException(INVALID_JWT);
        }
    }

    /**
     * Cookie에서 원하는 토큰을 찾아주는 메서드 (tokenPrefix를 통해서 찾는다)
     */
    public String resolveTokenFromCookie(Cookie[] cookies, JwtRule tokenPrefix) {
        try {
            return Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(tokenPrefix.getValue()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse("");
        } catch (BusinessException e) {
            log.error(REFRESH_TOKEN_NOT_FOUNT.getMessage());
            throw new BusinessException(REFRESH_TOKEN_NOT_FOUNT);
        }
    }

    /**
     * 시크릿 키를 찾아오는 메서드?
     */
    public Key getSigningKey(String secretKey) {
        String encodedKey = encodeToBase64(secretKey);
        return Keys.hmacShaKeyFor(encodedKey.getBytes(StandardCharsets.UTF_8));
    }

    private String encodeToBase64(String secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

}
