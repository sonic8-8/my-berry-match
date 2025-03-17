package com.gongcha.berrymatch.match.Service;

import com.gongcha.berrymatch.match.DTO.MatchingResultDto;
import com.gongcha.berrymatch.match.Repository.MatchRepository;
import com.gongcha.berrymatch.match.Repository.MatchingQueueRepository;
import com.gongcha.berrymatch.match.domain.*;
import com.gongcha.berrymatch.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    private final MatchRepository matchRepository;  // 매치 정보를 관리하는 리포지토리
    private final MatchingQueueRepository matchingQueueRepository;  // 매칭 대기열을 관리하는 리포지토리
    private final UserStatusUpdateService userStatusUpdateService;  // 유저 상태 업데이트를 위한 서비스
    private final MatchCompletionService matchCompletionService;  // 매칭 완료 처리를 위한 서비스

    // 미완성 매칭 결과들을 저장하기 위한 리스트
    private final List<MatchingResultDto> incompleteMatchingResults = new ArrayList<>();

    // 생성자를 통해 의존성 주입
    public MatchService(MatchRepository matchRepository, MatchingQueueRepository matchingQueueRepository, UserStatusUpdateService userStatusUpdateService, MatchCompletionService matchCompletionService) {
        this.matchRepository = matchRepository;
        this.matchingQueueRepository = matchingQueueRepository;
        this.userStatusUpdateService = userStatusUpdateService;
        this.matchCompletionService = matchCompletionService;
    }

    @Transactional
    public void processMatches(List<MatchingResultDto> matchingResults) {
        for (MatchingResultDto matchingResult : matchingResults) {
            boolean hasCancelledUser = false;
            Iterator<User> iterator = matchingResult.getMatchedUsers().iterator();

            while (iterator.hasNext()) {
                User user = iterator.next();
                Optional<MatchingQueue> currentQueueOpt = matchingQueueRepository.findByUserId(user.getId());

                if (currentQueueOpt.isPresent()) {
                    MatchingQueue currentQueue = currentQueueOpt.get();
                    if (currentQueue.getStatus() == MatchQueueStatus.CANCELLED) {
                        hasCancelledUser = true;
                        break;
                    }
                }
            }

            if (hasCancelledUser) {
                logger.info("Match creation cancelled due to a user cancellation in sport: {}", matchingResult.getSport());
                continue;
            }

            // 매칭 인원이 maxSize에 도달했는지 확인
            if (matchingResult.getMatchedUsers().size() == matchingResult.getMaxSize()) {
                logger.info("Processing match creation for sport: {} with {} users", matchingResult.getSport(), matchingResult.getMatchedUsers().size());
                Match match = createMatchTable(matchingResult);

                if (match != null) {
                    // 매치 완료된 사람들 매칭 대기열에서 상태 업데이트
                    userStatusUpdateService.updateUserStatusToMatched(match.getMatchUsers());


//                    // 노드로 보내줄 서비스
//                    // 매칭 완료된 유저의 ID를 가져옴 (ThreadLocal 사용)
//                    Long initiatingUserId = UserContext.getUserId();
//                    System.out.println("아이디 들어와야함"+initiatingUserId);
//                    if (initiatingUserId == null) {
//                        System.out.println("userId가 null입니다. ThreadLocal 값이 제거되었거나 다른 스레드에서 접근하고 있습니다.");
//                    }
//
//                    if (isUserInMatch(initiatingUserId, match.getMatchUsers())) {
//                        // 매칭 완료된 유저 ID를 매칭 완료 서비스로 전달
//                        matchCompletionService.completeMatch(initiatingUserId);
//                    }
                }
            } else {
                // 인원이 충족되지 않은 경우 임시 저장
                logger.info("Insufficient users for sport: {}. Current: {}, Required: {}",
                        matchingResult.getSport(),
                        matchingResult.getMatchedUsers().size(),
                        matchingResult.getMaxSize());

                incompleteMatchingResults.add(matchingResult);
            }
        }

        // 미완성 매칭 결과들을 다시 확인하여 인원이 충족된 경우 처리
        checkIncompleteMatches();
    }

    private Match createMatchTable(MatchingResultDto matchingResult) {
        try {
            logger.info("Creating match table for sport: {}", matchingResult.getSport());

            // 매치 테이블 생성 및 저장
            Match match = Match.builder()
                    .sport(matchingResult.getSport())
                    .matchedAt(matchingResult.getMatchTime())
                    .maxSize(matchingResult.getMaxSize())
                    .currentSize(0)
                    .matchStatus(MatchStatus.PRE_GAME)
                    .fullStatus(MatchFullStatus.EMPTY)
                    .build();

            matchRepository.saveAndFlush(match);  // 매치를 먼저 저장하여 match_id를 생성

            logger.info("Match table created with maxSize: {} and initial status: {}", matchingResult.getMaxSize(), MatchStatus.PRE_GAME);

            // 매칭된 유저들을 반으로 나누어 A팀과 B팀으로 배정
            int halfSize = matchingResult.getMatchedUsers().size() / 2;

            for (int i = 0; i < matchingResult.getMatchedUsers().size(); i++) {
                User user = matchingResult.getMatchedUsers().get(i);
                MatchTeam team = (i < halfSize) ? MatchTeam.A_Team : MatchTeam.B_Team;  // 반으로 나누어 팀 설정

                MatchUser matchUser = MatchUser.builder()
                        .match(match)  // match 필드를 설정
                        .user(user)
                        .status(MatchUserReady.WAITING)  // 기본 상태 설정
                        .team(team)  // A팀 또는 B팀 설정
                        .build();

                match.addUser(user, team);  // addUser 메서드가 matchUsers 리스트에 추가 처리
                logger.info("Added user with ID: {} to match for sport: {} and assigned to {}", user.getId(), matchingResult.getSport(), team);
            }

            matchRepository.save(match);  // matchUsers 리스트 업데이트를 위해 매치를 다시 저장
            logger.info("Match saved successfully with {} users for sport: {}", matchingResult.getMatchedUsers().size(), matchingResult.getSport());

            return match;

        } catch (Exception e) {
            logger.error("Error creating match table for sport: {}. Exception: {}", matchingResult.getSport(), e.getMessage(), e);
            throw new RuntimeException("Error creating match table", e);
        }
    }

//    private boolean isUserInMatch(Long userId, List<MatchUser> matchUsers) {
//        // 특정 유저가 매칭된 유저 리스트에 포함되어 있는지 확인
//        return matchUsers.stream()
//                .anyMatch(matchUser -> matchUser.getUser().getId().equals(userId));
//    }



    private void checkIncompleteMatches() {
        // 미완성 매칭 결과를 다시 확인하여 인원이 충족된 경우 처리
        Iterator<MatchingResultDto> iterator = incompleteMatchingResults.iterator();

        while (iterator.hasNext()) {
            MatchingResultDto incompleteResult = iterator.next();

            if (incompleteResult.getMatchedUsers().size() == incompleteResult.getMaxSize()) {
                logger.info("Completing previously incomplete match for sport: {}", incompleteResult.getSport());
                Match match = createMatchTable(incompleteResult);

                if (match != null) {
                    userStatusUpdateService.updateUserStatusToMatched(match.getMatchUsers());
                    iterator.remove();  // 인원이 충족된 경우 리스트에서 제거
                }
            }
        }
    }
}
