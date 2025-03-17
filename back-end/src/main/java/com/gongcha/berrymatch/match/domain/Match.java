package com.gongcha.berrymatch.match.domain;

import com.gongcha.berrymatch.chatMessage.ChatMessage;
import com.gongcha.berrymatch.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MatchStatus matchStatus;  // 매치의 상태 (예: PRE_GAME, IN_PROGRESS, FINISHED, CANCELLED)

    private int currentSize;  // 현재 매치에 참여한 유저 수

    private LocalDateTime matchedAt;  // 매치 생성 시간

    @Enumerated(EnumType.STRING)
    private Sport sport;  // 매치의 스포츠 종목

    @Enumerated(EnumType.STRING)
    private MatchFullStatus fullStatus;  // 매치의 완료 상태 (예: FULL, NOT_FULL, EMPTY)

    private int maxSize;  // 매치의 최대 인원 수

    // ChatMessage와의 일대다 관계 설정 (양방향 매핑)
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> chatMessages;  // 해당 매치(채팅방) 내 모든 메시지 리스트

    @Version
    private Long version = 1L;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MatchUser> matchUsers = new ArrayList<>();

    // 매치에 유저를 추가하는 메서드
    public void addUser(User user, MatchTeam team) {
        if (this.matchUsers == null) {
            this.matchUsers = new ArrayList<>();
        }
        MatchUser matchUser = MatchUser.builder()
                .match(this)
                .user(user)
                .status(MatchUserReady.WAITING)
                .team(team)
                .build();
        this.matchUsers.add(matchUser);
        this.currentSize = this.matchUsers.size();
        if (this.currentSize >= this.maxSize) {
            this.fullStatus = MatchFullStatus.FULL;
        } else if (this.currentSize > 0) {
            this.fullStatus = MatchFullStatus.NOT_FULL;
        } else {
            this.fullStatus = MatchFullStatus.EMPTY;
        }
    }


    // 상태를 설정하는 메서드
    public void setStatus(MatchFullStatus status) {
        this.fullStatus = status;
    }

    public void getStatus(MatchStatus matchStatus){this.matchStatus=matchStatus;}


}
