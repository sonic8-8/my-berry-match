package com.gongcha.berrymatch.match.DTO;


import com.gongcha.berrymatch.match.domain.MatchType;
import com.gongcha.berrymatch.match.domain.Sport;
import com.gongcha.berrymatch.user.City;
import com.gongcha.berrymatch.user.District;
import com.gongcha.berrymatch.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchingQueueDTO {

    //MatchRequestProcessingService 에서 매칭대기열에 저장할때 쓰는 DTO

    private Long id;                  // 매칭 대기열 ID
    private Long userId;              // 유저 ID
    private City city;                // 유저의 시
    private District district;        // 유저의 구
    private LocalDateTime matchTime;  // 매칭 시간
    private Sport sport;              // 스포츠 종목
    private String groupCode;         // 그룹 코드 (없을 수 있음)
    private MatchType matchType;      // 매칭 타입 (개인 또는 그룹)
    private LocalDateTime enqueuedAt; // 매칭 대기열에 추가된 시간

    // 유저 객체를 반환하는 메서드 (필요에 따라 추가)
    public User getUser() {
        return new User(this.userId, this.city, this.district);
    }

}
