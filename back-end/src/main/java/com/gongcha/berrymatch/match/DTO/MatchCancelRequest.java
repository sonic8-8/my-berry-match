package com.gongcha.berrymatch.match.DTO;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 매칭 취소 요청 DTO
 * 유저 식별자
 * 매치 취소 완료/실패 전송해줄 메시지
 */
@Data
@NoArgsConstructor
public class MatchCancelRequest {
    private String id;
    private String message;

    @Builder
    public MatchCancelRequest(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public MatchCancelServiceRequest toServiceRequest() {
        return MatchCancelServiceRequest.builder()
                .id(Long.valueOf(id))
                .message(message)
                .build();
    }
}
