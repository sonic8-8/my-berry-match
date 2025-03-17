package com.gongcha.berrymatch.springSecurity.responseDTO;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CodeToTokenResponse {
    Long userId;
    String identifier;

    public CodeToTokenResponse(Long userId, String identifier) {
        this.userId = userId;
        this.identifier = identifier;
    }
}
