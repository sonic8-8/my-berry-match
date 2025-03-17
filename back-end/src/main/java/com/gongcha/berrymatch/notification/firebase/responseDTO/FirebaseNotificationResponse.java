package com.gongcha.berrymatch.notification.firebase.responseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FirebaseNotificationResponse {

    String messageId;

    @Builder
    public FirebaseNotificationResponse(String messageId) {
        this.messageId = messageId;
    }
}
