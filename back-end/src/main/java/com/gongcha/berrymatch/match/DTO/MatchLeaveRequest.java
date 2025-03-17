package com.gongcha.berrymatch.match.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchLeaveRequest {

    private String id;

    public MatchLeaveServiceRequest toServiceRequest() {
        return MatchLeaveServiceRequest.builder()
                .id(Long.valueOf(id))
                .build();
    }
}
