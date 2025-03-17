package com.gongcha.berrymatch.notification.firebase.requestDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FirebaseNotificationRequest {

    private String userId;
    private String title;
    private String body;

    public FirebaseNotificationServiceRequest toServiceRequest() {
        return FirebaseNotificationServiceRequest.builder()
                .userId(Long.valueOf(userId))
                .title(title)
                .body(body)
                .build();
    }
}
