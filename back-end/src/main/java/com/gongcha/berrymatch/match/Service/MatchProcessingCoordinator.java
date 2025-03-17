package com.gongcha.berrymatch.match.Service;

import com.gongcha.berrymatch.match.DTO.MatchingResultDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchProcessingCoordinator {

    private final MatchService matchService;

    public MatchProcessingCoordinator(MatchService matchService) {
        this.matchService = matchService;
    }

    /**
     * 매칭 그룹을 받아 매치 테이블을 생성하도록 MatchService에 전달합니다.
     * @param matchingGroups 매칭 그룹 리스트
     */
    public void processMatchingGroups(List<MatchingResultDto> matchingGroups) {
        if (matchingGroups.isEmpty()) {
            System.out.println("No matching groups to process.");
            return;
        }
        matchService.processMatches(matchingGroups);
        System.out.println("Matching groups processed: " + matchingGroups.size());
    }


}
