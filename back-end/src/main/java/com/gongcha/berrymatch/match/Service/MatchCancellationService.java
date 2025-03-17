//package com.gongcha.berrymatch.match.Service;
//
//import com.gongcha.berrymatch.group.GroupRepository;
//import com.gongcha.berrymatch.match.Repository.MatchingQueueRepository;
//import com.gongcha.berrymatch.match.domain.MatchQueueStatus;
//import com.gongcha.berrymatch.match.domain.MatchingQueue;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class MatchCancellationService {
//
//    private static final Logger logger = LoggerFactory.getLogger(MatchCancellationService.class);
//
//    private final MatchingQueueRepository matchingQueueRepository;
//    private final GroupRepository groupRepository;
//
//    @Autowired
//    public MatchCancellationService(MatchingQueueRepository matchingQueueRepository,
//                                    GroupRepository groupRepository) {
//        this.matchingQueueRepository = matchingQueueRepository;
//        this.groupRepository = groupRepository;
//    }
//
//    /**
//     * 개인 매칭 취소를 처리하는 메서드.
//     *
//     * @param userId 매칭을 취소할 사용자의 ID
//     * @return 취소가 성공했는지 여부
//     */
//    public boolean cancelMatch(Long userId) {
//        logger.info("매칭 취소 요청 처리 시작: 유저 ID = {}", userId);
//
//        Optional<MatchingQueue> matchingQueueOptional = matchingQueueRepository.findByUserIdAndStatus(userId, MatchQueueStatus.PENDING);
//
//        if (matchingQueueOptional.isEmpty()) {
//            logger.warn("매칭 대기열에서 PENDING 상태의 유저를 찾을 수 없음: 유저 ID = {}", userId);
//            return false;
//        }
//
//        MatchingQueue matchingQueue = matchingQueueOptional.get();
//
//        // 이미 MATCHED 상태라면 취소할 수 없음
//        if (matchingQueue.getStatus() == MatchQueueStatus.MATCHED) {
//            logger.warn("이미 매칭된 유저의 취소 요청: 유저 ID = {}", userId);
//            return false;
//        }
//
//        matchingQueue.setStatus(MatchQueueStatus.CANCELLED); // 상태를 CANCELLED로 변경
//        matchingQueueRepository.save(matchingQueue); // 변경 사항 저장
//
//        logger.info("매칭 취소 처리 완료: 유저 ID = {}", userId);
//        return true;
//    }
//
//
//    /**
//     * 그룹 매칭 취소를 처리하는 메서드.
//     * 그룹 코드에 따라 그룹 내 모든 사용자의 매칭을 취소합니다.
//     *
//     * @param groupCord 매칭을 취소할 그룹의 코드
//     * @return 취소된 유저 수
//     */
//    public int cancelGroupMatch(String groupCord) {
//        logger.info("그룹 매칭 취소 요청 처리 시작: 그룹 코드 = {}", groupCord);
//
//        List<MatchingQueue> groupMembers = matchingQueueRepository.findByGroupCordAndStatus(groupCord, MatchQueueStatus.PENDING);
//
//        if (groupMembers.isEmpty()) {
//            logger.warn("매칭 대기열에서 PENDING 상태의 그룹을 찾을 수 없음: 그룹 코드 = {}", groupCord);
//            return 0;
//        }
//
//        int cancelledCount = 0;
//        for (MatchingQueue member : groupMembers) {
//            // 이미 MATCHED 상태인 경우 취소하지 않음
//            if (member.getStatus() != MatchQueueStatus.MATCHED) {
//                member.setStatus(MatchQueueStatus.CANCELLED); // 상태를 CANCELLED로 변경
//                matchingQueueRepository.save(member); // 변경 사항 저장
//                cancelledCount++;
//            }
//        }
//
//        logger.info("그룹 매칭 취소 처리 완료: 취소된 유저 수 = {}", cancelledCount);
//        return cancelledCount;
//    }
//}
