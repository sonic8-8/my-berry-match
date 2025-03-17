package com.gongcha.berrymatch.springSecurity.info.impl;



import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import com.gongcha.berrymatch.springSecurity.info.OAuth2UserInfo;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {
    private String providerId;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super((Map<String, Object>) attributes.get(ProviderInfo.KAKAO.getAttributeKey()));
        this.providerId = String.valueOf(attributes.get(ProviderInfo.KAKAO.getIdentifier()));
    }

//    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
//        super(attributes);
//    }

    @Override
    public String getProviderCode() {
        return providerId;
    }

    @Override
    public String getIdentifier() {
        return (String) attributes.get(ProviderInfo.KAKAO.getIdentifier());
    }
}
