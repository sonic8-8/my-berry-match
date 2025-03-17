package com.gongcha.berrymatch.match.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchLeaveServiceRequest {

    private Long id;

    @Builder
    public MatchLeaveServiceRequest(Long id) {
        this.id = id;
    }
}
