package com.gongcha.berrymatch.match.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DummyMatchRequest {

    //클라이언트 매칭요청 DTO

    private String sport;  // 스포츠 종목
    private String time;   // 매칭 시간 (예: 14:00)
    private String groupCode;  // 그룹 코드
    private String date;   // 매칭 날짜 (예: 2024-08-23)
    private String nickname;

    public MatchRequest toMatchRequest(Long id) {
        return MatchRequest.builder()
                .id(id)
                .sport(sport)
                .time(time)
                .groupCode(groupCode)
                .date(date)
                .build();
    }

    public MatchServiceRequest2 toMatchServiceRequest2(Long id) {
        return MatchServiceRequest2.builder()
                .id(id)
                .date(date)
                .groupCode(groupCode)
                .sport(sport)
                .time(time)
                .build();
    }
}
