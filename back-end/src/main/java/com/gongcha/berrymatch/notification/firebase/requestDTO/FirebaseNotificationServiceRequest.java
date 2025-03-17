package com.gongcha.berrymatch.notification.firebase.requestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FirebaseNotificationServiceRequest {

    private Long userId;
    private String title;
    private String body;

    @Builder
    public FirebaseNotificationServiceRequest(Long userId, String title, String body) {
        this.userId = userId;
        this.title = title;
        this.body = body;
    }
}
