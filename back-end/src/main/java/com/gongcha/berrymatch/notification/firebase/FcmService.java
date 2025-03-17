package com.gongcha.berrymatch.notification.firebase;

import com.gongcha.berrymatch.exception.BusinessException;
import com.gongcha.berrymatch.exception.ErrorCode;
import com.gongcha.berrymatch.notification.firebase.requestDTO.FirebaseNotificationServiceRequest;
import com.gongcha.berrymatch.notification.firebase.responseDTO.FirebaseNotificationResponse;
import com.gongcha.berrymatch.user.User;
import com.gongcha.berrymatch.user.UserRepository;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmService {
    private final UserRepository userRepository;
//    private final FcmTokenRepository fcmTokenRepository;

    /**
     * Firebase를 통해 푸시 알림을 전송합니다.
     * @return        전송된 메시지의 ID
     * @throws FirebaseMessagingException FCM 전송 중 오류 발생 시
     */
    public FirebaseNotificationResponse sendNotification(FirebaseNotificationServiceRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        System.out.println("FCM 토큰임 : " + user.getFcmToken());

        Notification notification = Notification.builder()
                .setTitle(request.getTitle())
                .setBody(request.getBody())
                .build();

        Message message = Message.builder()
                .setToken(user.getFcmToken())
                .setNotification(notification)
                .build();

        try {
            return FirebaseNotificationResponse.builder()
                    .messageId(FirebaseMessaging.getInstance().send(message))
                    .build();
        } catch (FirebaseMessagingException e) {
            if (e.getMessagingErrorCode().equals(MessagingErrorCode.INVALID_ARGUMENT)) {
                // 토큰이 유효하지 않은 경우, 오류 코드를 반환
                return FirebaseNotificationResponse.builder()
                        .messageId(e.getMessagingErrorCode().toString())
                        .build();
            } else if (e.getMessagingErrorCode().equals(MessagingErrorCode.UNREGISTERED)) {
                // 재발급된 이전 토큰인 경우, 오류 코드를 반환
                return FirebaseNotificationResponse.builder()
                        .messageId(e.getMessagingErrorCode().toString())
                        .build();
            }
            else { // 그 외, 오류는 런타임 예외로 처리
                throw new RuntimeException(e);
            }
        }
    }
}
