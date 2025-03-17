package com.gongcha.berrymatch.chatMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageResponse {

    private List<String> message;
    private Long matchid;
    private String nickname;
    private Long id;
    private LocalDateTime createdAt;

}
