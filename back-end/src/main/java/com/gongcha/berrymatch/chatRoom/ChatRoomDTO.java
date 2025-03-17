//package com.gongcha.berrymatch.chatRoom;
//
//import com.gongcha.berrymatch.chatMessage.ChatMessage;
//import com.gongcha.berrymatch.user.User;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Getter
//@NoArgsConstructor
//public class ChatRoomDTO {
//
//    private Long id;
//    private String chatName;
//    private List<User> users;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//    private List<ChatMessage> chatMessages;
//
//    @Builder
//    public ChatRoomDTO(String chatName, List<User> users){
//        this.chatName = chatName;
//        this.users = users;
//    }
//}
