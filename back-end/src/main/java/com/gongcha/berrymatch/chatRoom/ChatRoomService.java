package com.gongcha.berrymatch.chatRoom;


import com.gongcha.berrymatch.match.Repository.MatchRepository;
import com.gongcha.berrymatch.match.Repository.MatchUserRepository;
import com.gongcha.berrymatch.match.domain.MatchUser;
import com.gongcha.berrymatch.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomService {

    private final MatchUserRepository matchUserRepository;
    private final UserRepository userRepository;

    public ChatRoomService(UserRepository userRepository, MatchRepository matchRepository, MatchUserRepository matchUserRepository, UserRepository userRepository1) {
        this.matchUserRepository = matchUserRepository;
        this.userRepository = userRepository1;
    }


    public ChatRoomResponse getChatRoom(ChatRoomRequest chatRoomRequest) {
        // userId로 매칭된 MatchUser 조회
        MatchUser matchUser = matchUserRepository.findByUserId(chatRoomRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저의 매칭이 없습니다."));

        String nickname = matchUser.getUser().getNickname();
        // MatchUser에서 Match 객체를 가져와서 matchId 반환
        Long matchId = matchUser.getMatch().getId();
        return new ChatRoomResponse(matchId ,nickname);
    }


}
