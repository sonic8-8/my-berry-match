package com.gongcha.berrymatch.chatMessage;

import com.gongcha.berrymatch.match.Repository.MatchRepository;
import com.gongcha.berrymatch.match.domain.Match;
import com.gongcha.berrymatch.user.User;
import com.gongcha.berrymatch.user.UserRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class ChatMessageService {


    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;  // User를 찾기 위한 리포지토리
    private final MatchRepository matchRepository;  // Match를 찾기 위한 리포지토리

    public ChatMessageService(ChatMessageRepository chatMessageRepository, UserRepository userRepository, MatchRepository matchRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
    }


    // 비동기로 메시지 저장
    @Async
    @Transactional
    public void saveMessage(ChatMessageRequest chatMessageRequest) {
        // ChatMessageDTO에서 정보 추출
        Long matchId = chatMessageRequest.getMatchId();
        Long userId = chatMessageRequest.getId();
        String message = chatMessageRequest.getMessage();

        // User와 Match 객체를 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 유저 ID: " + userId));
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 매치 ID: " + matchId));

        // ChatMessage 엔티티를 빌더 패턴으로 생성
        ChatMessage chatMessage = ChatMessage.builder()
                .user(user)  // 조회한 유저 객체
                .match(match)  // 조회한 매치 객체
                .message(message)  // 메시지 내용
                .build();

        // 메시지를 데이터베이스에 저장
        chatMessageRepository.save(chatMessage);

        System.out.println("메시지가 저장되었습니다: " + message + " - 매치아이디: " + matchId + ", 유저아이디: " + userId);
    }





    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getMessages(Long matchId) {
        // 데이터베이스에서 매치 ID에 해당하는 모든 메시지를 시간순으로 가져오기
        List<ChatMessage> messages = chatMessageRepository.findByMatchIdOrderByCreatedAtAsc(matchId);

        // 메시지 엔티티를 응답 DTO로 변환하여 반환 (빌더 패턴 사용)
        return messages.stream()
                .map(message -> ChatMessageResponse.builder()  // 빌더 패턴으로 ChatMessageResponse 생성
                        .message(List.of(message.getMessage()))  // 단일 메시지를 리스트로 변환
                        .matchid(message.getMatch().getId())  // 매치 ID
                        .id(message.getUser().getId())  // 유저 ID
                .nickname(message.getUser().getNickname())
                        .createdAt(message.getCreatedAt())  // 생성 시간
                        .build())
                .collect(Collectors.toList());
    }



}
