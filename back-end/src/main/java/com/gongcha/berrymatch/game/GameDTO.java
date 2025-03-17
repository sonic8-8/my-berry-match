package com.gongcha.berrymatch.game;

import com.gongcha.berrymatch.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GameDTO {

    private Game game;

    private GameResultTemp gameResultTemp;

    private User user;

    private int resultTeamA;

    private int resultTeamB;

    private GameRecordTempStatus gameRecordTempStatus;

    private int votes;

//    @Builder
//    public GameDTO(Game game, User user, int resultTeamA, int resultTeamB, GameRecordTempStatus gameRecordTempStatus,int votes){
//        this.game = game;
//        this.user = user;
//        this.resultTeamA = resultTeamA;
//        this.resultTeamB = resultTeamB;
//        this.gameRecordTempStatus = gameRecordTempStatus;
//        this.votes = votes;
//    }



}
