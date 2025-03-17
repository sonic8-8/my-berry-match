package com.gongcha.berrymatch.chatMessage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gongcha.berrymatch.match.domain.Match;
import com.gongcha.berrymatch.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Builder
@Entity
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore  // 직렬화 시 순환 참조를 방지하기 위해 무시
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    @JsonIgnore  // 직렬화 시 순환 참조를 방지하기 위해 무시
    private Match match;

    @Column(nullable = false)
    private String message;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;  // 메시지 생성 시간
}
