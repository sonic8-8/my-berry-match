package com.gongcha.berrymatch.game.requestDTO;

import com.gongcha.berrymatch.game.GameEndVoteStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GameEndVoteServiceRequest {

    private Long userId;
    private String nickname;
    private GameEndVoteStatus gameEndVoteStatus;

    @Builder
    public GameEndVoteServiceRequest(Long userId, String nickname, GameEndVoteStatus gameEndVoteStatus) {
        this.userId = userId;
        this.nickname = nickname;
        this.gameEndVoteStatus = gameEndVoteStatus;
    }
}
