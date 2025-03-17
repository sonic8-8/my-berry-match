package com.gongcha.berrymatch.match.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchBoomRequest {

    private String id;

    @Builder
    public MatchBoomRequest(String id) {
        this.id = id;
    }

    public MatchBoomServiceRequest toServiceRequest() {
        return MatchBoomServiceRequest.builder()
                .id(Long.valueOf(id))
                .build();
    }
}
