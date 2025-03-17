package com.gongcha.berrymatch.springSecurity.controller;


import com.gongcha.berrymatch.ApiResponse;
import com.gongcha.berrymatch.exception.BusinessException;
import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import com.gongcha.berrymatch.springSecurity.domain.UserPrincipal;
import com.gongcha.berrymatch.springSecurity.requestDTO.TokenRequest;
import com.gongcha.berrymatch.springSecurity.responseDTO.AuthResponse;
import com.gongcha.berrymatch.springSecurity.responseDTO.LogoutResponse;
import com.gongcha.berrymatch.springSecurity.service.JwtFacade;
import com.gongcha.berrymatch.springSecurity.service.TokenService;
import com.gongcha.berrymatch.user.User;
import com.gongcha.berrymatch.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gongcha.berrymatch.exception.ErrorCode.DUPLICATED_REFRESH_TOKEN;
import static com.gongcha.berrymatch.exception.ErrorCode.NOT_AUTHENTICATED_USER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final UserService userService;
    private final JwtFacade jwtFacade;
    private final TokenService tokenService;

    @PostMapping("/auth")
    public ApiResponse<AuthResponse> generateToken(HttpServletResponse response,
                                             @RequestBody TokenRequest request) {

        System.out.println("토큰 발행 요청 들어옴");

        User user = userService.findUserByOAuthInfo(request.toTokenServiceRequest().getIdentifier(), request.toTokenServiceRequest().getProviderInfo());

        if (!user.isRegistered()) {
            throw new BusinessException(NOT_AUTHENTICATED_USER);
        }

        jwtFacade.generateAccessToken(response, user);
        jwtFacade.generateRefreshToken(response, user);
        jwtFacade.setReissuedHeader(response);

        AuthResponse authResponse = userService.getUserAuthInfo(user.getIdentifier(), user.getProviderInfo());

        System.out.println("토큰 발행함");

        return ApiResponse.ok(authResponse);
    }

    @PostMapping("/logout")
    public ApiResponse<LogoutResponse> logout(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            HttpServletResponse response) {

        LogoutResponse result = LogoutResponse.builder()
                .message(jwtFacade.logout(response, userPrincipal.getUser().getIdentifier(), userPrincipal.getUser().getProviderInfo()))
                .build();

        return ApiResponse.ok(result);
    }

    @PostMapping("/auth/refresh")
    public ApiResponse<AuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = jwtFacade.resolveRefreshToken(request);
        String identifier = jwtFacade.getIdentifierFromRefresh(refreshToken);
        ProviderInfo providerInfo = jwtFacade.getProviderInfoFromRefresh(refreshToken);

        if (tokenService.isRefreshDuplicate(identifier, providerInfo)) {
            throw new BusinessException(DUPLICATED_REFRESH_TOKEN);
        }

        if (refreshToken == null || !jwtFacade.validateRefreshToken(refreshToken, identifier, providerInfo)) {
            throw new BusinessException(NOT_AUTHENTICATED_USER); // Refresh token이 유효하지 않거나 없을 때 예외 처리
        }

        User user = userService.findUserByOAuthInfo(identifier, providerInfo);

        tokenService.deleteAllByIdentifierAndProviderInfo(identifier, providerInfo); // 기존의 refresh 토큰 삭제

        jwtFacade.generateAccessToken(response, user);
        jwtFacade.setReissuedHeader(response);
        jwtFacade.generateRefreshToken(response, user); // 보안 강화를 위해 refresh 토큰도 재발급 하기로 함

        AuthResponse authResponse = userService.getUserAuthInfo(user.getIdentifier(), user.getProviderInfo());

        return ApiResponse.ok(authResponse);
    }



}
