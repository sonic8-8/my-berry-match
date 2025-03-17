package com.gongcha.berrymatch.match.Service;

import com.gongcha.berrymatch.match.Repository.MatchUserRepository;
import com.gongcha.berrymatch.match.domain.Match;
import com.gongcha.berrymatch.match.domain.MatchStatus;
import com.gongcha.berrymatch.match.domain.MatchTeam;
import com.gongcha.berrymatch.match.domain.MatchUser;
import com.gongcha.berrymatch.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MatchCompletionService {

    private static final Logger logger = LoggerFactory.getLogger(MatchCompletionService.class);
    private final WebClient webClient;
    private final MatchUserRepository matchUserRepository;

    public MatchCompletionService(WebClient.Builder webClientBuilder, MatchUserRepository matchUserRepository) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:9000").build();
        this.matchUserRepository = matchUserRepository;
    }

    @Transactional
    public void completeMatch(Long userId) {
        logger.info("Received userId for match completion: {}", userId);

        if (userId == null) {
            logger.error("No user ID provided.");
            return;
        }

        // 게임 시작 전 또는 진행 중인 매치만 조회
        List<MatchStatus> validStatuses = List.of(MatchStatus.PRE_GAME, MatchStatus.IN_PROGRESS);
        logger.info("Fetching match users for userId: {} with match status: {}", userId, validStatuses);

        // 주어진 userId로 매칭된 매치들 중 경기 시작 전 또는 경기 중인 매치들을 가져옴
        List<MatchUser> matchUsers = matchUserRepository.findByUser_IdAndMatch_MatchStatusIn(userId, validStatuses);

        if (matchUsers.isEmpty()) {
            logger.error("No matching users found for the provided user ID.");
            return;
        }

        logger.info("Found match users for userId: {}. Total users: {}", userId, matchUsers.size());

        // 첫 번째 매칭된 유저의 매치 정보 가져오기 (동일 매치에 속해 있어야 함)
        Match match = matchUsers.get(0).getMatch();
        logger.info("Found match with matchId: {} for userId: {}", match.getId(), userId);

        // 매칭 정보에서 A팀과 B팀으로 유저를 나눔
        List<User> teamAUsers = new ArrayList<>();
        List<User> teamBUsers = new ArrayList<>();

        for (MatchUser matchUser : matchUsers) {
            if (matchUser.getTeam() == MatchTeam.A_Team) {
                teamAUsers.add(matchUser.getUser());
                logger.info("Added userId: {} to Team A", matchUser.getUser().getId());
            } else if (matchUser.getTeam() == MatchTeam.B_Team) {
                teamBUsers.add(matchUser.getUser());
                logger.info("Added userId: {} to Team B", matchUser.getUser().getId());
            }
        }

        logger.info("Total Team A users: {}, Total Team B users: {}", teamAUsers.size(), teamBUsers.size());

        // A팀과 B팀 유저 정보를 노드 서버로 전송
        sendToNodeServer(teamAUsers, teamBUsers, userId);
    }

    /**
     * A팀과 B팀 유저 정보를 Node.js 서버로 전송
     */
    private void sendToNodeServer(List<User> teamAUsers, List<User> teamBUsers, Long userId) {
        try {
            // 페이로드 데이터 생성
            Map<String, Object> payload = createPayload(teamAUsers, teamBUsers, userId);
            logger.info("Payload created for Node.js server: {}", payload);

            // 데이터 전송
            webClient.post()
                    .uri("/api/match/completed")
                    .body(BodyInserters.fromValue(payload))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .subscribe(
                            response -> logger.info("Successfully sent match data to Node server."),
                            error -> logger.error("Failed to send match data to Node server: " + error.getMessage())
                    );
        } catch (Exception e) {
            logger.error("Error occurred while sending match data to Node server: " + e.getMessage());
        }
    }

    /**
     * A팀과 B팀 유저 정보를 포함한 페이로드 생성
     */
    private Map<String, Object> createPayload(List<User> teamAUsers, List<User> teamBUsers, Long userId) {
        Map<String, Object> payload = new HashMap<>();
        List<Map<String, Object>> teamAUserInfos = new ArrayList<>();
        List<Map<String, Object>> teamBUserInfos = new ArrayList<>();

        // A팀 유저 정보 추가
        for (User user : teamAUsers) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("name", user.getNickname());
            userInfo.put("team", "A");
            teamAUserInfos.add(userInfo);
        }

        // B팀 유저 정보 추가
        for (User user : teamBUsers) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("name", user.getNickname());
            userInfo.put("team", "B");
            teamBUserInfos.add(userInfo);
        }

        // 페이로드에 팀 정보 추가
        payload.put("teamAUsers", teamAUserInfos);
        payload.put("teamBUsers", teamBUserInfos);

        // 매칭을 요청한 유저 ID 추가
        payload.put("requestingUserId", userId);

        logger.info("Payload created: {}", payload);
        return payload;
    }
}
