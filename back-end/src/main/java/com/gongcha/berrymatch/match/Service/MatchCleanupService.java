package com.gongcha.berrymatch.match.Service;

import com.gongcha.berrymatch.match.Repository.MatchRepository;
import com.gongcha.berrymatch.match.domain.Match;
import com.gongcha.berrymatch.match.domain.MatchStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchCleanupService {

    private final MatchRepository matchRepository;

    // 주기적으로 매치 상태를 조회하고 FINISHED 상태인 매치를 삭제
    @Scheduled(fixedRate = 60000)  // 1분마다 실행 (원하는 주기로 설정 가능)
    @Async("MatchEenTaskExecutor")
    @Transactional
    public void cleanupFinishedMatches() {
        // FINISHED 상태의 모든 매치 조회
        List<Match> finishedMatches = matchRepository.findByMatchStatus(MatchStatus.FINISHED);

        // FINISHED 상태의 매치들을 삭제
        for (Match match : finishedMatches) {
            matchRepository.delete(match);
        }
    }
}
