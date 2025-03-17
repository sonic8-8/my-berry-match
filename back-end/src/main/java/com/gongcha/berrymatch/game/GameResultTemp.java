package com.gongcha.berrymatch.game;

import com.gongcha.berrymatch.match.domain.MatchUser;
import com.gongcha.berrymatch.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class GameResultTemp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @Enumerated(EnumType.STRING)
    private GameRecordTempStatus gameRecordTempStatus = GameRecordTempStatus.BEFORE_RECORD;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private User user;

    private int resultTeamA;

    private int resultTeamB;

    private int votes;

    @Builder
    public GameResultTemp(Game game, User user, int resultTeamA, int resultTeamB, GameRecordTempStatus gameRecordTempStatus,int votes){
        this.game = game;
        this.user = user;
        this.resultTeamA = resultTeamA;
        this.resultTeamB = resultTeamB;
        this.gameRecordTempStatus = gameRecordTempStatus;
        this.votes = votes;
    }

    public void setGameRecordTempStatus(GameRecordTempStatus gameRecordTempStatus){
        this.gameRecordTempStatus = gameRecordTempStatus;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
