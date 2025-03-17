package com.gongcha.berrymatch.springSecurity.requestDTO;

import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenServiceRequest {
    private String identifier;
    private ProviderInfo providerInfo;

    @Builder
    public TokenServiceRequest(String identifier, ProviderInfo providerInfo) {
        this.identifier = identifier;
        this.providerInfo = providerInfo;
    }
}
