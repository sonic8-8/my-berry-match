package com.gongcha.berrymatch.match.Service;

import com.gongcha.berrymatch.match.Repository.MatchingQueueRepository;
import com.gongcha.berrymatch.match.domain.MatchQueueStatus;
import com.gongcha.berrymatch.match.domain.MatchingQueue;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
//매칭 대기열 비우는 서비스
@Service
@RequiredArgsConstructor
public class MatchingQueueCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(MatchingQueueCleanupService.class);

    private final MatchingQueueRepository matchingQueueRepository;

    /**
     * 일정 주기로 매칭 대기열에서 MATCHED 및 CANCELLED 상태의 유저들을 삭제합니다.
     */
    @Scheduled(fixedRate = 360000) // 1시간마다 실행 조절가능
    @Transactional
    public void cleanUpMatchingQueue() {
        // MATCHED 상태인 유저들 삭제
        List<MatchingQueue> matchedUsers = matchingQueueRepository.findByStatus(MatchQueueStatus.MATCHED);
        for (MatchingQueue matchingQueue : matchedUsers) {
            matchingQueueRepository.delete(matchingQueue);
            logger.info("Deleted MATCHED user with ID: {}", matchingQueue.getUser().getId());
        }

        // CANCELLED 상태인 유저들 삭제
        List<MatchingQueue> cancelledUsers = matchingQueueRepository.findByStatus(MatchQueueStatus.CANCELLED);
        for (MatchingQueue matchingQueue : cancelledUsers) {
            matchingQueueRepository.delete(matchingQueue);
            logger.info("Deleted CANCELLED user with ID: {}", matchingQueue.getUser().getId());
        }
    }
}
