package com.gongcha.berrymatch.notification;

import com.gongcha.berrymatch.ApiResponse;
import com.gongcha.berrymatch.notification.requestDTO.NotificationRequest;
import com.gongcha.berrymatch.notification.responseDTO.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    /**
     * SSE 방출기를 연결시켜 실시간 알림을 받을 수 있게 해주는 HTTP 메서드
     */
    @GetMapping("/stream")
    public SseEmitter getConnect(@RequestParam("userId") String userId) {
        return notificationService.createSseEmitter(Long.valueOf(userId));
    }

//    /**
//     * SSE 테스트 알림을 보내주는 HTTP 메서드
//     */
//    @PostMapping("/stream/notify")
//    public ApiResponse<NotificationResponse> notify(@RequestBody NotificationRequest request) {
//        return ApiResponse.ok(notificationService.sendNotification(request.toServiceRequest().getUserId()));
//    }

//    /**
//     * 메인 대시보드에 실시간으로 매칭 상태 알림을 보내주는 테스트용 HTTP 메서드
//     */
//    @PostMapping("/stream/matchStatus")
//    public ApiResponse<NotificationResponse> matchStatus(@RequestBody NotificationRequest request) throws IOException {
//        return ApiResponse.ok(notificationService.sendMatchStatus(request.toServiceRequest().getUserId()), request.toServiceRequest().getUserId());
//    }

    // 좋아요 눌렀을때 알림

    // 랭킹 알림

    // 퀘스트 알림

}