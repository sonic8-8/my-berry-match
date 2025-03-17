package com.gongcha.berrymatch.chatRoom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResponse {
    private Long matchId;
    private String nickname;

    public ChatRoomResponse(Long matchId) {
        this.matchId = matchId;
    }


}
