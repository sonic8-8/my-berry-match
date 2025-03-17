package com.gongcha.berrymatch.springSecurity.config;

import com.gongcha.berrymatch.springSecurity.filter.ExceptionHandlerFilter;
import com.gongcha.berrymatch.springSecurity.filter.JwtAuthenticationFilter;
import com.gongcha.berrymatch.springSecurity.handler.OAuth2FailureHandler;
import com.gongcha.berrymatch.springSecurity.handler.OAuth2SuccessHandler;
import com.gongcha.berrymatch.springSecurity.service.CustomOAuth2UserService;
import com.gongcha.berrymatch.springSecurity.service.JwtFacade;
import com.gongcha.berrymatch.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@Order(1)
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    public static final String PERMITTED_URI[] = {"/api/auth/**", "/login"};
    private static final String PERMITTED_ROLES[] = {"USER", "ADMIN"};
    private final CustomCorsConfigurationSource customCorsConfigurationSource;
    private final CustomOAuth2UserService customOAuthService; // OAuth 관련
    private final JwtFacade jwtFacade;
    private final UserService userService;
    private final OAuth2SuccessHandler successHandler; // OAuth 관련
    private final OAuth2FailureHandler failureHandler; // OAuth 관련

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors(corsCustomizer -> corsCustomizer
                        .configurationSource(customCorsConfigurationSource)
                )
                // csrf 비활성화
                .csrf(CsrfConfigurer::disable)
                // http basic 로그인 비활성화
                .httpBasic(HttpBasicConfigurer::disable)
                // form login 비활성화
                .formLogin(FormLoginConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        // ADMIN만 접근 가능
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // 안전하지 않은 HTTP 메서드 등은 PreFlight 요청을 보냄. 특정 요청을 허용할지 묻는 역할
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        // PERMITTED_URI는 모두 접근 가능
                        .requestMatchers(PERMITTED_URI).permitAll()
                        // 그 외의 요청들은 PERMITTED_ROLES(USER, ADMIN) 중 하나라도 가지고 있어야 접근 가능
                        .requestMatchers("/api/stream/**").authenticated()
                        // SSE 엔드포인트는 인증됨
                        .requestMatchers("/api/fcm/**").authenticated()
                        // firebase cloud messaging 엔드포인트는 인증됨
                        .requestMatchers("/api/boom").authenticated()
                        .anyRequest().hasAnyRole(PERMITTED_ROLES))


                // JWT 사용으로 인한 세션 미사용
                .sessionManagement(configurer -> configurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // JWT 검증 필터 추가
                .addFilterBefore(new JwtAuthenticationFilter(jwtFacade, userService),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(), JwtAuthenticationFilter.class)

                // OAuth 로그인 설정
                .oauth2Login(customConfigurer -> customConfigurer
                        .userInfoEndpoint(endpointConfig -> endpointConfig.userService(customOAuthService))
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)

                );

        return http.build();
    }
}
