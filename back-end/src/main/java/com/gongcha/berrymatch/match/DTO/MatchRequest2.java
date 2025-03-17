package com.gongcha.berrymatch.match.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchRequest2 {

    private String id;       // 유저 ID
    private String sport;  // 스포츠 종목
    private String time;   // 매칭 시간 (예: 14:00)
    private String groupCode;  // 그룹 코드
    private String date;   // 매칭 날짜 (예: 2024-08-23)

    @Builder
    public MatchRequest2(String id, String sport, String time, String groupCode, String date) {
        this.id = id;
        this.sport = sport;
        this.time = time;
        this.groupCode = groupCode;
        this.date = date;
    }

    public MatchServiceRequest2 toMatchServiceRequest2() {
        return MatchServiceRequest2.builder()
                .date(date)
                .groupCode(groupCode)
                .id(Long.valueOf(id))
                .sport(sport)
                .time(time)
                .build();
    }


}
