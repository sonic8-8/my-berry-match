package com.gongcha.berrymatch.notification.firebase.responseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FcmTokenRegisterResponse {
    private String identifier;
    private String providerInfo;
    private String fcmToken;

    @Builder
    public FcmTokenRegisterResponse(String identifier, String providerInfo, String fcmToken) {
        this.identifier = identifier;
        this.providerInfo = providerInfo;
        this.fcmToken = fcmToken;
    }
}
