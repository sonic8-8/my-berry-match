package com.gongcha.berrymatch.game;

import com.gongcha.berrymatch.match.domain.Match;
import com.gongcha.berrymatch.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long id;

    private String gameTitle;

    private LocalDateTime matchedAt; // Match 엔티티의 데이터를 옮겨 받을 필드

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users;

    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    private int resultTeamA;

    private int resultTeamB;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public Game(String gameTitle, List<User> users, LocalDateTime matchedAt, GameStatus gameStatus) {
        this.gameTitle = gameTitle;
        this.users = users;
        this.matchedAt = matchedAt; // Match 엔티티의 데이터를 받음
        this.gameStatus = gameStatus;
    }

    // 상태 변경 메서드
    public void finishGame() {
        this.gameStatus = GameStatus.RECORDING_COMPLETED;
    }

    public void setResultTeamA(int resultTeamA) {
        this.resultTeamA = resultTeamA;
    }

    public void setResultTeamB(int resultTeamB) {
        this.resultTeamB = resultTeamB;
    }

}
