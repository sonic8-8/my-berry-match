package com.gongcha.berrymatch.notification.requestDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationRequest {

    private String userId;

    public NotificationServiceRequest toServiceRequest() {
        return NotificationServiceRequest.builder()
                .userId(Long.valueOf(userId))
                .build();
    }
}
