package com.gongcha.berrymatch.springSecurity.info.impl;



import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import com.gongcha.berrymatch.springSecurity.info.OAuth2UserInfo;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super((Map<String, Object>) attributes.get(ProviderInfo.NAVER.getAttributeKey()));
    }

    @Override
    public String getProviderCode() {
        return (String) attributes.get(ProviderInfo.NAVER.getProviderCode());
    }

    @Override
    public String getIdentifier() {
        return (String) attributes.get(ProviderInfo.NAVER.getIdentifier());
    }
}
