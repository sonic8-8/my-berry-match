package com.gongcha.berrymatch.springSecurity.info;

import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import com.gongcha.berrymatch.springSecurity.info.impl.GoogleOAuth2UserInfo;
import com.gongcha.berrymatch.springSecurity.info.impl.KakaoOAuth2UserInfo;
import com.gongcha.berrymatch.springSecurity.info.impl.NaverOAuth2UserInfo;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(ProviderInfo providerInfo, Map<String, Object> attributes) {
        switch (providerInfo) {
            case KAKAO -> {
                return new KakaoOAuth2UserInfo(attributes);
            }
            case NAVER -> {
                return new NaverOAuth2UserInfo(attributes);
            }
            case GOOGLE -> {
                return new GoogleOAuth2UserInfo(attributes);
            }
        }
        throw new OAuth2AuthenticationException("INVALID PROVIDER TYPE");
    }
}
