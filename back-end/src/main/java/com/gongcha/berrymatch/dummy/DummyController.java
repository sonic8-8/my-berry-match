package com.gongcha.berrymatch.dummy;

import com.gongcha.berrymatch.ApiResponse;
import com.gongcha.berrymatch.dummy.requestDTO.DummyLeaveRequest;
import com.gongcha.berrymatch.dummy.requestDTO.DummyReadyRequest;
import com.gongcha.berrymatch.dummy.requestDTO.DummyVoteRequest;
import com.gongcha.berrymatch.dummy.requestDTO.DummyWaitingRequest;
import com.gongcha.berrymatch.exception.BusinessException;
import com.gongcha.berrymatch.exception.ErrorCode;
import com.gongcha.berrymatch.match.DTO.DummyMatchCancelRequest;
import com.gongcha.berrymatch.match.DTO.DummyMatchRequest;
import com.gongcha.berrymatch.match.DTO.DummyMatchResponse;
import com.gongcha.berrymatch.match.Repository.MatchRepository;
import com.gongcha.berrymatch.match.Repository.MatchUserRepository;
import com.gongcha.berrymatch.match.Service.MatchCancelService;
import com.gongcha.berrymatch.match.Service.MatchRequestProcessingService;
import com.gongcha.berrymatch.match.domain.*;
import com.gongcha.berrymatch.user.User;
import com.gongcha.berrymatch.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DummyController {

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final MatchRequestProcessingService matchRequestProcessingService;
    private final MatchCancelService matchCancelService;
    private final MatchUserRepository matchUserRepository;

    /**
     * 더미 데이터의 매칭을 요청하는 메서드
     */
    @PostMapping("/dummy-matching")
    public ApiResponse<String> dummyMatching(@RequestBody DummyMatchRequest request) {
        try {

            User user = userRepository.findByNickname(request.getNickname())
                    .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_UPDATED));

            // 매칭 요청 처리 서비스 호출
            matchRequestProcessingService.processMatchRequest(request.toMatchServiceRequest2(user.getId()));

            return ApiResponse.ok("대기열 입장중");

        } catch (Exception e) {
            // 이미 매칭 중이거나 대기 중인 유저에 대한 예외 처리
            return ApiResponse.of(HttpStatus.CONFLICT, "이미 매칭 중입니다.", "매칭요청실패");
        }
    }

    /**
     * 더미 데이터의 매칭을 취소하는 메서드
     */
    @PostMapping("/dummy-cancel")
    public ApiResponse<DummyMatchResponse> dummyCancelMatch(@RequestBody DummyMatchCancelRequest request) throws IOException {

        User user = userRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_UPDATED));

        matchCancelService.cancelMatching(request.toMatchCancelServiceRequest(user.getId()));

        return ApiResponse.ok(null);
    }

    /**
     * 더미데이터의 준비 상태를 READY로 업데이트 하는 메서드
     */
    @PostMapping("/dummy-ready")
    @Transactional
    public ApiResponse<?> dummyReadyUpdate(@RequestBody DummyReadyRequest request) {

        User user = userRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        MatchUser matchUser = matchUserRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        matchUser.updateMatchUserReady(MatchUserReady.READY);

        matchUserRepository.save(matchUser);

        return ApiResponse.ok(matchUser.getStatus());
    }

    /**
     * 더미데이터의 준비 상태를 WAITING으로 업데이트 하는 메서드
     */
    @PostMapping("/dummy-waiting")
    @Transactional
    public ApiResponse<?> dummyWaitingUpdate(@RequestBody DummyWaitingRequest request) {

        User user = userRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        MatchUser matchUser = matchUserRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        matchUser.updateMatchUserReady(MatchUserReady.WAITING);

        matchUserRepository.save(matchUser);

        return ApiResponse.ok(matchUser.getStatus());
    }

    /**
     * 더미데이터가 매칭을 떠나게 하는 메서드
     */
    @PostMapping("/dummy-leave")
    @Transactional
    public ApiResponse<?> dummyLeave(@RequestBody DummyLeaveRequest request) {

        User user = userRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        MatchUser matchUser = matchUserRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Match match = matchUser.getMatch();

        // 매치가 경기 중인지 확인
        if (match.getMatchStatus() == MatchStatus.IN_PROGRESS) {
            throw new IllegalStateException("경기 중에는 매치를 떠날 수 없습니다.");
        }

        // 매치유저 엔티티를 삭제
        matchUserRepository.delete(matchUser);

        // 매치에서 current_size를 감소
        match.setCurrentSize(match.getCurrentSize() - 1);

        // current_size가 max_size보다 작다면 상태를 NOT_FULL로 변경
        if (match.getCurrentSize() < match.getMaxSize()) {
            match.setStatus(MatchFullStatus.NOT_FULL);
        }

        // 매치에 남아있는 유저가 없는 경우 매치 상태를 EMPTY로 설정
        if (match.getCurrentSize() == 0) {
            match.setStatus(MatchFullStatus.EMPTY);
        }

        // 변경된 매치 정보를 저장
        matchRepository.save(match);

        return ApiResponse.ok("삭제 완료");
    }

    /**
     * 더미데이터의 경기 종료 투표 상태를 업데이트 하는 메서드
     */
    @PostMapping("/dummy-vote-")
    @Transactional
    public ApiResponse<?> dummyVote(@RequestBody DummyVoteRequest request) {

        User user = userRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        MatchUser matchUser = matchUserRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        matchUser.updateMatchUserReady(MatchUserReady.WAITING);

        matchUserRepository.save(matchUser);

        return ApiResponse.ok(matchUser.getStatus());

    }

}