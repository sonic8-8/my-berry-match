package com.gongcha.berrymatch.springSecurity.info;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public abstract String getProviderCode();

    public abstract String getIdentifier();
}
