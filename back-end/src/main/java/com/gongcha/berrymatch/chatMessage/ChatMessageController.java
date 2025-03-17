package com.gongcha.berrymatch.chatMessage;

import com.gongcha.berrymatch.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    /**
     * DB에 메세지 내용 저장
     * @param chatMessage
     * @return String
     */
    @PostMapping("/chat/save-msg")
    public ApiResponse<String> saveMessage(@RequestBody ChatMessageRequest chatMessage){
        System.out.println("메시지들어옴"+chatMessage);
        chatMessageService.saveMessage(chatMessage);
        return ApiResponse.ok("Message saved successfully");
    }


    @GetMapping("/chat/{MatchId}")
    public ApiResponse<List<ChatMessageResponse>> loadALlMessages(@PathVariable Long MatchId){
        System.out.println("메시지내역불러오기아이디들어옴"+MatchId);
      List <ChatMessageResponse> chatMessageResponse = chatMessageService.getMessages(MatchId);
        System.out.println("내보내는 리스트"+chatMessageResponse);
        return ApiResponse.ok(chatMessageResponse);
    }


}
