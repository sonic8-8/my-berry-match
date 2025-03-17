package com.gongcha.berrymatch.springSecurity.service;


import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import com.gongcha.berrymatch.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface JwtFacade {
    String generateAccessToken(HttpServletResponse response, User user);

    String generateRefreshToken(HttpServletResponse response, User user);

    String resolveAccessToken(HttpServletRequest request);

    String resolveRefreshToken(HttpServletRequest request);

    String getIdentifierFromRefresh(String refreshToken);

    ProviderInfo getProviderInfoFromRefresh(String refreshToken);

    boolean validateAccessToken(String accessToken);

    boolean validateRefreshToken(String refreshToken, String identifier, ProviderInfo providerInfo);

    void setReissuedHeader(HttpServletResponse response);

    String logout(HttpServletResponse response, String identifier, ProviderInfo providerInfo);

    Authentication getAuthentication(String accessToken);

    void deleteRefreshToken(String identifier, ProviderInfo providerInfo);

    boolean isRefreshTokenDuplicate(String identifier, ProviderInfo providerInfo);
}
