package com.gongcha.berrymatch.user.requestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DummyDeleteServiceRequest {
    private String nickname;

    @Builder
    public DummyDeleteServiceRequest(String nickname) {
        this.nickname = nickname;
    }
}
