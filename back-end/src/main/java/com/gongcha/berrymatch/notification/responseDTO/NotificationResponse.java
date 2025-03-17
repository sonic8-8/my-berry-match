package com.gongcha.berrymatch.notification.responseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationResponse {

    private String message;

    @Builder
    public NotificationResponse(String message) {
        this.message = message;
    }
}
