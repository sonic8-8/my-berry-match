package com.gongcha.berrymatch.match.DTO;

import com.gongcha.berrymatch.user.UserMatchStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchCancelResponse {

    private String nickname;
    private UserMatchStatus userMatchStatus;

    @Builder
    public MatchCancelResponse(String nickname, UserMatchStatus userMatchStatus) {
        this.nickname = nickname;
        this.userMatchStatus = userMatchStatus;
    }
}
