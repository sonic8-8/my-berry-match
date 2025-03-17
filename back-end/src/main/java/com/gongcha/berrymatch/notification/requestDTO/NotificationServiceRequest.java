package com.gongcha.berrymatch.notification.requestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationServiceRequest {

    private Long userId;

    @Builder
    public NotificationServiceRequest(Long userId) {
        this.userId = userId;
    }
}
