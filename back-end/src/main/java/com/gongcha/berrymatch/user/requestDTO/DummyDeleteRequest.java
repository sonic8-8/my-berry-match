package com.gongcha.berrymatch.user.requestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DummyDeleteRequest {
    private String nickname;

    @Builder
    public DummyDeleteRequest(String nickname) {
        this.nickname = nickname;
    }

    public DummyDeleteServiceRequest toServiceRequest() {
        return DummyDeleteServiceRequest.builder()
                .nickname(nickname)
                .build();
    }
}
