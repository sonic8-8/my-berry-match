package com.gongcha.berrymatch.springSecurity.info.impl;



import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import com.gongcha.berrymatch.springSecurity.info.OAuth2UserInfo;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getProviderCode() {
        return (String) attributes.get(ProviderInfo.GOOGLE.getProviderCode());
    }

    @Override
    public String getIdentifier() {
        return (String) attributes.get(ProviderInfo.GOOGLE.getIdentifier());
    }
}
