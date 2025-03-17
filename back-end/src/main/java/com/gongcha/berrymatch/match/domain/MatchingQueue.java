package com.gongcha.berrymatch.match.domain;

import com.gongcha.berrymatch.user.City;
import com.gongcha.berrymatch.user.District;
import com.gongcha.berrymatch.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    //클라이언트가 원하는 시간에서 범위로뺌 +-1시간
    @Builder.Default
    private LocalDateTime requestTime = LocalDateTime.now();

    //매칭대기열테이블 유저 상태
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private MatchQueueStatus status = MatchQueueStatus.PENDING;

    //지역 시
    @Enumerated(EnumType.STRING)
    private City city;

    //지역구
    @Enumerated(EnumType.STRING)
    private District district;

    //매치요청시 클라이언트가 원하는시간
    private LocalDateTime matchTime;

    //매치 종목
    @Enumerated(EnumType.STRING)
    private Sport sport;

    //그룹코드
    private String groupCode;

    //그룹매칭인지 개인매칭인지 상태
    @Enumerated(EnumType.STRING)
    private MatchType matchType;

    //락킹
    @Version // Optimistic locking field

    private Long version;
    // 매칭 대기열에 들어간 시간
    private LocalDateTime enqueuedAt;

}
