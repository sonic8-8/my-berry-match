package com.gongcha.berrymatch.match.Service;

import com.gongcha.berrymatch.match.DTO.MatchCancelRequest;
import com.gongcha.berrymatch.match.DTO.MatchCancelServiceRequest;
import com.gongcha.berrymatch.match.Repository.MatchingQueueRepository;
import com.gongcha.berrymatch.match.domain.MatchQueueStatus;
import com.gongcha.berrymatch.match.domain.MatchingQueue;
import com.gongcha.berrymatch.notification.NotificationService;
import com.gongcha.berrymatch.notification.firebase.FcmService;
import com.gongcha.berrymatch.notification.firebase.requestDTO.FirebaseNotificationServiceRequest;
import com.gongcha.berrymatch.user.User;
import com.gongcha.berrymatch.user.UserMatchStatus;
import com.gongcha.berrymatch.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchCancelService {

    private final MatchingQueueRepository matchingQueueRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final FcmService fcmService;

    /**
     * 매칭 취소 요청을 처리합니다.
     * matchRequest.getID();취소할 유저의 ID
     */
    @Transactional
    public void cancelMatching(MatchCancelServiceRequest request) throws IOException {

        // 매칭 대기열에서 해당 유저 찾기
        Optional<MatchingQueue> matchingQueueOpt = matchingQueueRepository.findByUserId(request.getId());

        if (matchingQueueOpt.isPresent()) {
            MatchingQueue matchingQueue = matchingQueueOpt.get();

            // 유저의 상태가 PENDING인 경우에만 취소 처리
            if (matchingQueue.getStatus() == MatchQueueStatus.PENDING) {
                matchingQueue.setStatus(MatchQueueStatus.CANCELLED);
                matchingQueueRepository.save(matchingQueue);

                User user = matchingQueue.getUser();

                user.updateMatchStatus(UserMatchStatus.NOT_MATCHED);
                userRepository.save(user);

                // SSE 알림
                notificationService.createSseEmitter(user.getId());
                notificationService.sendMatchStatus(user.getId(), user.getUserMatchStatus());

                // FCM 알림
                if (user.getFcmToken() != null) {

                    FirebaseNotificationServiceRequest fcmRequest = FirebaseNotificationServiceRequest.builder()
                            .userId(user.getId())
                            .title("매칭 상태 업데이트")
                            .body("매칭이 취소되었습니다.")
                            .build();
                    fcmService.sendNotification(fcmRequest);
                }

            } else {
                throw new IllegalStateException("Cannot cancel matching as it is already in progress or finished.");
            }
        } else {
            throw new RuntimeException("Matching queue entry not found for user ID: " + request.getId());
        }
    }
}
