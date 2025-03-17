package com.gongcha.berrymatch.match.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchBoomServiceRequest {

    private Long id;

    @Builder
    public MatchBoomServiceRequest(Long id) {
        this.id = id;
    }
}
