package com.gongcha.berrymatch.springSecurity.domain;

import org.springframework.security.web.csrf.CsrfToken;

public class CustomCsrfToken implements CsrfToken {
    @Override
    public String getHeaderName() {
        return null;
    }

    @Override
    public String getParameterName() {
        return null;
    }

    @Override
    public String getToken() {
        return null;
    }
}
