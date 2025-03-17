package com.gongcha.berrymatch.chatRoom;

import com.gongcha.berrymatch.ApiResponse;
import com.gongcha.berrymatch.chatMessage.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    /**
     * 매칭 완료된 사용자들의 채팅방에 대한 Data DB에 등록
     */
    @PostMapping("/chat/room")
    public ApiResponse<ChatRoomResponse> createNewChatRoom(@RequestBody ChatRoomRequest chatRoomRequest){
        System.out.println("신호 들어옴" + chatRoomRequest.getId());
      ChatRoomResponse chatRoomResponse =  chatRoomService.getChatRoom(chatRoomRequest);
        return ApiResponse.ok(chatRoomResponse);
    }

//    /**
//     * 해당 채팅방의 정보를 보내줌
//     */
//    @GetMapping("/chatRoom/{roomId}/info")
//    public ApiResponse<ChatRoom> loadChatRoomInfo(@PathVariable Long roomId){
//        return ApiResponse.ok(chatRoomService.loadChatRoomInfo(roomId));
//    }

//    /**
//     * DB에서 해당하는 채팅방의 메세지 내용 불러오기
//     */
//    @GetMapping("/chatRoom/{roomId}/msgs")
//    public ApiResponse<List<ChatMessage>> loadMessages(@PathVariable Long roomId){
//        return ApiResponse.ok(chatMessageService.loadAllMessages(roomId));
//    }

}
