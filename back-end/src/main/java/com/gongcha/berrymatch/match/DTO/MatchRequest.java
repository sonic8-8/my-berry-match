package com.gongcha.berrymatch.match.DTO;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Getter
public class MatchRequest {

    //클라이언트 매칭요청 DTO
    private Long id;       // 유저 ID
    private String sport;  // 스포츠 종목
    private String time;   // 매칭 시간 (예: 14:00)
    private String groupCode;  // 그룹 코드
    private String date;   // 매칭 날짜 (예: 2024-08-23)

    @Builder
    public MatchRequest(Long id, String sport, String time, String groupCode, String date) {
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
                .sport(sport)
                .time(time)
                .build();
    }

    @JsonCreator
    public MatchRequest(@JsonProperty("id") String id) {
        try {
            this.id = Long.parseLong(id);
        } catch (NumberFormatException e) {
            this.id = null; // 또는 적절한 예외 처리
        }
    }
}