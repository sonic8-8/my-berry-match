package com.gongcha.berrymatch.springSecurity.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

import static com.gongcha.berrymatch.springSecurity.constants.JwtRule.ACCESS_HEADER;
import static com.gongcha.berrymatch.springSecurity.constants.JwtRule.ACCESS_REISSUED_HEADER;

@Component
public class CustomCorsConfigurationSource implements CorsConfigurationSource {
    private final String ALLOWED_ORIGIN;
    private final List<String> ALLOWED_METHODS = List.of("POST", "GET", "PATCH", "OPTIONS", "DELETE");

    public CustomCorsConfigurationSource(@Value("${url.base}") String BASE_URL) {
        ALLOWED_ORIGIN = BASE_URL;
    }

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList(ALLOWED_ORIGIN));
        config.setAllowedMethods(ALLOWED_METHODS);
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Collections.singletonList("*"));

        config.setExposedHeaders(Collections.singletonList(ACCESS_HEADER.getValue()));
        config.addExposedHeader(ACCESS_REISSUED_HEADER.getValue());
        
        config.setMaxAge(3600L);
        return config;
    }
}
