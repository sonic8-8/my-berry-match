package com.gongcha.berrymatch.springSecurity.responseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LogoutResponse {
    private String message;

    @Builder
    public LogoutResponse(String message) {
        this.message = message;
    }
}
