package com.gongcha.berrymatch.springSecurity.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum JwtRule {
    ACCESS_HEADER("Authorization"),
    ACCESS_PREFIX("Bearer "),
    ACCESS_REISSUED_HEADER("token-reissued"),

    REFRESH_PREFIX("refresh"),

    REFRESH_ISSUE("Set-Cookie"),
    REFRESH_RESOLVE("Cookie");

    private final String value;
}