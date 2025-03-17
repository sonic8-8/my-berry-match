package com.gongcha.berrymatch.match.Controller;

import com.gongcha.berrymatch.ApiResponse;
import com.gongcha.berrymatch.chatMessage.ChatMessageRepository;
import com.gongcha.berrymatch.chatMessage.ChatMessageService;
import com.gongcha.berrymatch.exception.BusinessException;
import com.gongcha.berrymatch.exception.ErrorCode;
import com.gongcha.berrymatch.game.requestDTO.GameEndVoteRequest;
import com.gongcha.berrymatch.match.DTO.*;
import com.gongcha.berrymatch.match.Repository.MatchRepository;
import com.gongcha.berrymatch.match.Repository.MatchUserRepository;
import com.gongcha.berrymatch.match.Service.GetMatchUserService;
import com.gongcha.berrymatch.match.Service.MatchCancelService;
import com.gongcha.berrymatch.match.Service.MatchReadyService;
import com.gongcha.berrymatch.match.Service.MatchRequestProcessingService;
import com.gongcha.berrymatch.match.ThreadLocal.UserContext;
import com.gongcha.berrymatch.match.domain.Match;
import com.gongcha.berrymatch.match.domain.MatchUser;
import com.gongcha.berrymatch.notification.NotificationService;
import com.gongcha.berrymatch.notification.firebase.FcmService;
import com.gongcha.berrymatch.notification.firebase.requestDTO.FirebaseNotificationServiceRequest;
import com.gongcha.berrymatch.user.User;
import com.gongcha.berrymatch.user.UserMatchStatus;
import com.gongcha.berrymatch.user.UserRepository;
import com.gongcha.berrymatch.user.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MatchController {
    private static final Logger logger = LoggerFactory.getLogger(MatchController.class);
    private final MatchRequestProcessingService matchRequestProcessingService;
    private final MatchCancelService matchCancelService;
    private final MatchReadyService matchReadyService;
    private final GetMatchUserService getMatchUserService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final UserService userService;
    private final FcmService fcmService;
    private final MatchUserRepository matchUserRepository;
    private final ChatMessageService chatMessageService;
    private final ChatMessageRepository chatMessageRepository;
    private final MatchRepository matchRepository;

    @Autowired
    public MatchController(MatchRequestProcessingService matchRequestProcessingService, MatchCancelService cancelMatching, MatchCancelService matchCancelService, MatchReadyService matchReadyService, GetMatchUserService getMatchUserService,
                           UserRepository userRepository, NotificationService notificationService, UserService userService, FcmService fcmService, MatchUserRepository matchUserRepository, ChatMessageService chatMessageService, ChatMessageRepository chatMessageRepository, MatchRepository matchRepository) {
        this.matchRequestProcessingService = matchRequestProcessingService;
        this.matchCancelService = matchCancelService;
        this.matchReadyService = matchReadyService;
        this.getMatchUserService = getMatchUserService;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.userService = userService;
        this.fcmService = fcmService;
        this.matchUserRepository = matchUserRepository;
        this.chatMessageService = chatMessageService;

        this.chatMessageRepository = chatMessageRepository;
        this.matchRepository = matchRepository;
    }

    /**
     * 매칭 요청을 처리하는 엔드포인트.
     *
     * @param request 매칭 요청 DTO
     * @return 매칭 처리 결과 응답
     */
    @Transactional
    @PostMapping("/matching")
    public ApiResponse<String> matching(@RequestBody MatchRequest2 request) {

            UserContext.setUserId(Long.valueOf(request.getId()));

            System.out.println("컨트롤러"+UserContext.getUserId());

            System.out.println("매칭 요청 들어옴");

            // 매칭 요청 처리 서비스 호출
            matchRequestProcessingService.processMatchRequest(request.toMatchServiceRequest2());

            return ApiResponse.ok("대기열 입장중");
    }


    @GetMapping("/matchusers")
    public ApiResponse<List<MatchUserResponse>> getMatchUser(@ModelAttribute MatchUserRequest matchUserRequest){
        System.out.println("매칭페이지 진입"+matchUserRequest.getId());
        List<MatchUserResponse> matchUserResponses = getMatchUserService.getMatchUser(matchUserRequest);

        return ApiResponse.ok(matchUserResponses);
    }




/**
 * 유저의 매칭 취소 요청을 처리하는 엔드포인트
 *
 * @param request 매칭 취소요청 DTO
 * @return 성공 메시지
 */
@PostMapping("/cancel")
public ApiResponse<MatchCancelResponse> cancelMatch(@RequestBody MatchCancelRequest request) {
    try {

        matchCancelService.cancelMatching(request.toServiceRequest());

        System.out.println("취소 완료");

        User user = userRepository.findById(request.toServiceRequest().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_UPDATED));

        user.updateMatchStatus(UserMatchStatus.NOT_MATCHED);
        userRepository.save(user);

        // SSE 알림
        notificationService.createSseEmitter(user.getId());
        notificationService.sendMatchStatus(user.getId(), user.getUserMatchStatus());

        // FCM 푸시 알림
        if (user.getFcmToken() != null) {

            FirebaseNotificationServiceRequest fcmRequest = FirebaseNotificationServiceRequest.builder()
                    .title("매칭 상태 업데이트")
                    .body("매칭이 취소됐습니다.")
                    .userId(user.getId())
                    .build();

            fcmService.sendNotification(fcmRequest);
        }

        MatchCancelResponse response = MatchCancelResponse.builder()
                .nickname(user.getNickname())
                .userMatchStatus(user.getUserMatchStatus())
                .build();

        return ApiResponse.ok(response);
    } catch (Exception e) {
        return ApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage(), null);
    }
}

    /**
     * 준비완료
     * @param matchReadyRequest 준비완로 신호
     * @return
     */
    @PostMapping("/ready")
    public ApiResponse<MatchReadyRequest> ReadyMatch(@RequestBody MatchReadyRequest matchReadyRequest){

        System.out.println("신호들어옴"+ matchReadyRequest.getId());
        MatchReadyRequest response= matchReadyService.userReadyStatus(matchReadyRequest);
        if (response.isAllUsersReady()) {
            return ApiResponse.ok(response);
        } else {
            return ApiResponse.ok(response);
        }
    }


    /**
     * 준비 취소
     * @param matchReadyRequest 준비취소 신호
     * @return
     */
    @PostMapping("/waiting")
    public ApiResponse<MatchResponse> WaitingMatch(@RequestBody MatchReadyRequest matchReadyRequest){
        matchReadyService.UserWitingStatus(matchReadyRequest);
        return ApiResponse.ok(null);
    }


    /**
     * 매치나가기 신호
     */
    @PostMapping("/match-leave")
    public ApiResponse<MatchResponse> MatchLeave(@RequestBody MatchLeaveRequest request){

        matchReadyService.UserMatchLeave(request.toServiceRequest());
        return ApiResponse.ok(null);
    }

    /**
     * 게임 종료 투표
     */
    @PostMapping("/gameEndVote")
    @Transactional
    public ApiResponse<?> GameEndVote(@RequestBody GameEndVoteRequest request){
        User user = userService.findUserById(request.toServiceRequest().getUserId());

        user.updateGameEndVoteStatus(request.toServiceRequest().getGameEndVoteStatus());

        userRepository.save(user);

        return ApiResponse.ok(user.getGameEndvoteStatus());

    }

    @PostMapping("/boom")
    @Transactional
    public ApiResponse<?> boom(@RequestBody MatchBoomRequest request) throws IOException {

        System.out.println("요청 들어옴");

        User user = userRepository.findById(request.toServiceRequest().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        user.updateMatchStatus(UserMatchStatus.NOT_MATCHED);
        userRepository.save(user);

        if (user.getFcmToken() != null) {

            FirebaseNotificationServiceRequest fcmRequest = FirebaseNotificationServiceRequest.builder()
                    .userId(user.getId())
                    .title("경기 종료")
                    .body("경기가 종료되었습니다")
                    .build();

            fcmService.sendNotification(fcmRequest);

        }

        System.out.println("채팅 삭제할게~");

        // chatMessage 삭제
        chatMessageRepository.deleteAll();

        System.out.println("매치 유저 삭제할게~");

        // matchUser 삭제
        matchUserRepository.deleteAll();

        System.out.println("매치 삭제할게~");

        // match 삭제
        matchRepository.deleteAll();

        return ApiResponse.ok("다 삭제함 ㅅㄱ");


    }

}
