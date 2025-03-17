package com.gongcha.berrymatch.match.DTO;

import com.gongcha.berrymatch.match.domain.MatchType;
import com.gongcha.berrymatch.match.domain.Sport;
import com.gongcha.berrymatch.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchingResultDto {

    //필터링하고 여기로 다시담아서 매치서비스로 넘기는중
    private List<User> matchedUsers;  // 매칭된 사용자 목록
    private MatchType matchType;      // 매칭 유형 ("GROUP" 또는 "USER")
    private Sport sport;              // 스포츠 종목
    private LocalDateTime matchTime;  // 매칭 요청 시간
    private int maxSize;              // 매칭의 최대 인원 수

}
