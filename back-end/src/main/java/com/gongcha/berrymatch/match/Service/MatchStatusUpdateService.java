//package com.gongcha.berrymatch.match.Service;
//
//import com.gongcha.berrymatch.match.Repository.MatchRepository;
//import com.gongcha.berrymatch.match.Repository.MatchUserRepository;
//import com.gongcha.berrymatch.match.domain.Match;
//import com.gongcha.berrymatch.match.domain.MatchFullStatus;
//import com.gongcha.berrymatch.match.domain.MatchStatus;
//import com.gongcha.berrymatch.match.domain.MatchUser;
//import jakarta.persistence.OptimisticLockException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.retry.annotation.Backoff;
//import org.springframework.retry.annotation.Retryable;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class MatchStatusUpdateService {
//
//    private final MatchRepository matchRepository;
//    private final MatchUserRepository matchUserRepository;
//    private static final Logger logger = LoggerFactory.getLogger(MatchStatusUpdateService.class);
//
//    @Autowired
//    public MatchStatusUpdateService(MatchRepository matchRepository, MatchUserRepository matchUserRepository) {
//        this.matchRepository = matchRepository;
//        this.matchUserRepository = matchUserRepository;
//    }
//
//    @Scheduled(fixedDelay = 100000)
//    @Retryable(
//            value = {OptimisticLockException.class},
//            maxAttempts = 3,
//            backoff = @Backoff(delay = 3000) // 1초 대기
//    )
//    public void updateMatchStatuses() {
//        List<Match> matches = matchRepository.findByMatchStatus(MatchStatus.PRE_GAME);
//
//        for (Match match : matches) {
//            try {
//                // Perform update within the transaction
//                List<MatchUser> matchUsers = matchUserRepository.findByMatch(match);
//                int currentSize = matchUsers.size();
//                match.setCurrentSize(currentSize);
//
//                if (currentSize == 0) {
//                    match.setMatchFullStatus(MatchFullStatus.EMPTY);
//                } else if (currentSize >= match.getMaxSize()) {
//                    match.setMatchFullStatus(MatchFullStatus.FULL);
//                } else {
//                    match.setMatchFullStatus(MatchFullStatus.NOT_FULL);
//                }
//
//                matchRepository.save(match);
//                logger.info("매치 ID {} 상태가 업데이트되었습니다", match.getId());
//            } catch (OptimisticLockException e) {
//                logger.warn("Optimistic lock exception while updating match: {}", match.getId(), e);
//                // Optionally add delay before retry
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException ex) {
//                    Thread.currentThread().interrupt();
//                    logger.error("Thread interrupted during retry", ex);
//                }
//            }
//        }
//    }
//}
