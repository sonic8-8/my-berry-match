package com.gongcha.berrymatch.springSecurity.filter;


import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import com.gongcha.berrymatch.springSecurity.service.JwtFacade;
import com.gongcha.berrymatch.user.User;
import com.gongcha.berrymatch.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static com.gongcha.berrymatch.springSecurity.config.SecurityConfig.PERMITTED_URI;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtFacade jwtFacade;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (isPermittedURI(request.getRequestURI())) {
            SecurityContextHolder.getContext().setAuthentication(null);
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtFacade.resolveAccessToken(request);
        if (jwtFacade.validateAccessToken(accessToken)) {
            setAuthenticationToContext(accessToken);
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("access 토큰이 유효하지 않아서 refresh 토큰으로 재발급 시도");

        String refreshToken = jwtFacade.resolveRefreshToken(request);

        System.out.println("refresh 토큰 가져오기 성공");

        User user = findUserByRefreshToken(refreshToken);

        System.out.println("refresh 토큰으로 유저 정보까지 가져옴");

        if (!jwtFacade.isRefreshTokenDuplicate(user.getIdentifier(), user.getProviderInfo())) {

            System.out.println("refresh 토큰 중복 검증");


            if (jwtFacade.validateRefreshToken(refreshToken, user.getIdentifier(), user.getProviderInfo())) {

                System.out.println("refresh 토큰 검사 통과");

                String reissuedAccessToken = jwtFacade.generateAccessToken(response, user);

                System.out.println("access 토큰 생성");

                jwtFacade.setReissuedHeader(response);

                jwtFacade.deleteRefreshToken(user.getIdentifier(), user.getProviderInfo()); // MongoDB에서 삭제

                System.out.println("refresh 토큰 mongoDB에서 삭제");

                jwtFacade.generateRefreshToken(response, user); // 재발급

                System.out.println("refresh 토큰 재발급");

                jwtFacade.setReissuedHeader(response);

                setAuthenticationToContext(reissuedAccessToken);

                filterChain.doFilter(request, response);
                return;
            }
        } else {

            jwtFacade.deleteRefreshToken(user.getIdentifier(), user.getProviderInfo()); // MongoDB에서 삭제

            System.out.println("refresh 토큰 중복이라 mongoDB에서 삭제");

        }

        System.out.println("refresh 토큰도 유효하지 않아서 로그아웃 처리함");


        jwtFacade.logout(response, user.getIdentifier(), user.getProviderInfo());
    }

    private boolean isPermittedURI(String requestURI) {
        return Arrays.stream(PERMITTED_URI)
                .anyMatch(permitted -> {
                    String replace = permitted.replace("*", "");
                    return requestURI.contains(replace) || replace.contains(requestURI);
                });
    }

    private User findUserByRefreshToken(String refreshToken) {
        String identifier = jwtFacade.getIdentifierFromRefresh(refreshToken);
        System.out.println("identifier: " + identifier);
        ProviderInfo providerInfo = jwtFacade.getProviderInfoFromRefresh(refreshToken);
        System.out.println("providerInfo: " + providerInfo);
        return userService.findUserByOAuthInfo(identifier, providerInfo);
    }

    private void setAuthenticationToContext(String accessToken) {
        Authentication authentication = jwtFacade.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }



}
