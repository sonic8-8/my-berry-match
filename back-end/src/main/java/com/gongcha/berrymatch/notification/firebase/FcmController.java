package com.gongcha.berrymatch.notification.firebase;

import com.gongcha.berrymatch.ApiResponse;
import com.gongcha.berrymatch.notification.firebase.requestDTO.FcmTokenRegisterRequest;
import com.gongcha.berrymatch.notification.firebase.requestDTO.FirebaseNotificationRequest;
import com.gongcha.berrymatch.notification.firebase.responseDTO.FcmTokenRegisterResponse;
import com.gongcha.berrymatch.notification.firebase.responseDTO.FirebaseNotificationResponse;
import com.gongcha.berrymatch.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FcmController {

    private final FcmService fcmService;
    private final UserService userService;

    /**
     * User의 DB에 FCM 토큰을 등록해주는 HTTP 메서드
     */
    @PostMapping("/fcm/register")
    public ApiResponse<FcmTokenRegisterResponse> registerFcmToken(@RequestBody FcmTokenRegisterRequest request) {

        System.out.println("fcm 토큰 등록 요청 들어옴----------------------");
        System.out.println(request.getFcmToken());
        return ApiResponse.ok(userService.updateFcmToken(request.toServiceRequest()));
    }

    /**
     * 특정 사용자의 Id(index)로 알림을 보내주는 HTTP 메서드
     */
    @PostMapping("/fcm/notification")
    public ApiResponse<FirebaseNotificationResponse> sendNotification(@RequestBody FirebaseNotificationRequest request) {

        System.out.println("fcm 알림 전송 테스트 요청 들어옴----------------------");
        System.out.println(request.getBody());
        System.out.println(request.getTitle());
        System.out.println(request.getUserId());

        return ApiResponse.ok(fcmService.sendNotification(request.toServiceRequest()));
    }




}
