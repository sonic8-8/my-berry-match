package com.gongcha.berrymatch.springSecurity.domain;

import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oAuth2User;
    private final ProviderInfo providerInfo;
    private final String identifier;

    public CustomOAuth2User(OAuth2User oAuth2User, ProviderInfo providerInfo, String identifier) {
        this.oAuth2User = oAuth2User;
        this.providerInfo = providerInfo;
        this.identifier = identifier;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }

    public ProviderInfo getProviderInfo() {
        return providerInfo;
    }

    public String getIdentifier() {
        return identifier;
    }
}
