package com.gongcha.berrymatch.game.requestDTO;

import com.gongcha.berrymatch.game.GameEndVoteStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GameEndVoteRequest {

    private String userId;
    private String nickname;
    private String gameEndVoteStatus;

    @Builder
    public GameEndVoteRequest(String userId, String nickname, String gameEndVoteStatus) {
        this.userId = userId;
        this.nickname = nickname;
        this.gameEndVoteStatus = gameEndVoteStatus;
    }

    public GameEndVoteServiceRequest toServiceRequest() {
        return GameEndVoteServiceRequest.builder()
                .userId(Long.valueOf(userId))
                .nickname(nickname)
                .gameEndVoteStatus(GameEndVoteStatus.valueOf(gameEndVoteStatus))
                .build();
    }
}
