package com.gongcha.berrymatch.chatMessage;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatMessageRequest {
    private Long id;
    private Long matchId;
    private String message;



}
