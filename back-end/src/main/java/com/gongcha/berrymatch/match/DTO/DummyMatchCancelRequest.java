package com.gongcha.berrymatch.match.DTO;


import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 매칭 취소 요청 DTO
 * 유저 식별자
 * 매치 취소 완료/실패 전송해줄 메시지
 */
@Data
@NoArgsConstructor
public class DummyMatchCancelRequest {

    private String message;
    private String nickname;

    public MatchCancelServiceRequest toMatchCancelServiceRequest(Long id) {
        return MatchCancelServiceRequest.builder()
                .id(id)
                .message(message)
                .build();
    }
}
