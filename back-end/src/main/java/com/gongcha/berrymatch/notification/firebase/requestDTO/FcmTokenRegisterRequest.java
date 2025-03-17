package com.gongcha.berrymatch.notification.firebase.requestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FcmTokenRegisterRequest {

    String userId;
    String fcmToken;

    @Builder
    public FcmTokenRegisterRequest(String userId, String fcmToken) {
        this.userId = userId;
        this.fcmToken = fcmToken;
    }

    public FcmTokenRegisterServiceRequest toServiceRequest() {
        return FcmTokenRegisterServiceRequest.builder()
                .fcmToken(fcmToken)
                .userId(Long.valueOf(userId))
                .build();
    }
}
