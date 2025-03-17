package com.gongcha.berrymatch.notification.firebase.requestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FcmTokenRegisterServiceRequest {

    String fcmToken;
    Long userId;

    @Builder
    public FcmTokenRegisterServiceRequest(String fcmToken, Long userId) {
        this.fcmToken = fcmToken;
        this.userId = userId;
    }
}
