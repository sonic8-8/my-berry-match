package com.gongcha.berrymatch.springSecurity.service;

import com.gongcha.berrymatch.user.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT의 생성과 검증을 수행하는 클래스.
 * Provider는 일반적으로 생성 및 검증 기능을 함께 가진 클래스를 의미한다.
 */
@Component
public class JwtGenerator {

    private final long now = System.currentTimeMillis();

    /**
     * Access Toekn를 발급해주는 메서드
     */
    public String generateAccessToken(final Key ACCESS_SECRET, final long ACCESS_EXPIRATION, User user) {

        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(user))
                .setSubject(String.valueOf(user.getId()))
                .setExpiration(new Date(now + ACCESS_EXPIRATION))
                .signWith(ACCESS_SECRET, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Refresh Token을 발급해주는 메서드
     */
    public String generateRefreshToken(final Key REFRESH_SECRET, final long REFRESH_EXPIRATION, User user) {
        Long now = System.currentTimeMillis();

        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(user))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + REFRESH_EXPIRATION))
                .signWith(REFRESH_SECRET, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Token의 Header를 생성하는 메서드
     */
    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        return header;
    }

    private Map<String, Object> createClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("identifier", user.getIdentifier());
        claims.put("providerInfo", user.getProviderInfo());
        claims.put("role", user.getRole());
        return claims;
    }

}
