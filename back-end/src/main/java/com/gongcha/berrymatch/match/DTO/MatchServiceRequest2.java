package com.gongcha.berrymatch.match.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Data
public class MatchServiceRequest2 {

    private Long id;       // 유저 ID
    private String sport;  // 스포츠 종목
    private String time;   // 매칭 시간 (예: 14:00)
    private String groupCode;  // 그룹 코드
    private String date;   // 매칭 날짜 (예: 2024-08-23)

    @Builder
    public MatchServiceRequest2(Long id, String sport, String time, String groupCode, String date) {
        this.id = id;
        this.sport = sport;
        this.time = time;
        this.groupCode = groupCode;
        this.date = date;
    }

    public MatchRequest toMatchRequest() {
        return MatchRequest.builder()
                .date(date)
                .groupCode(groupCode)
                .id(id)
                .time(time)
                .sport(sport)
                .build();
    }


}
