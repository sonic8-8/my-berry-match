package com.gongcha.berrymatch.springSecurity.requestDTO;

import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRequest {
    private String identifier;
    private String providerInfo;

    @Builder
    public TokenRequest(String identifier,  String providerInfo) {
        this.identifier = identifier;
        this.providerInfo = providerInfo;
    }

    public ProviderInfo toProviderInfo(String providerInfo) {
        try {
            return ProviderInfo.from(providerInfo);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("providerInfo가 적절한 형태가 아닙니다 : " + providerInfo);
        }
    }

    public TokenServiceRequest toTokenServiceRequest() {
        return TokenServiceRequest.builder()
                .identifier(identifier)
                .providerInfo(toProviderInfo(providerInfo))
                .build();
    }
}
