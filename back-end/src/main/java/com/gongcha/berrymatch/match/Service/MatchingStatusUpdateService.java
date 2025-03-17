//package com.gongcha.berrymatch.match.Service;
//
//import com.gongcha.berrymatch.match.DTO.MatchStatusUpdateRequest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@Service
//public class MatchingStatusUpdateService {
//
//    private static final Logger logger = LoggerFactory.getLogger(MatchingStatusUpdateService.class);
//    private final WebClient webClient;
//
//    @Autowired
//    public MatchingStatusUpdateService(WebClient.Builder webClientBuilder) {
//        // WebClient의 기본 URL을 설정합니다.
//        this.webClient = webClientBuilder.baseUrl("http://localhost:9000").build();
//        logger.info("MatchingStatusUpdateService initialized with base URL: http://localhost:9000");
//    }
//
//    /**
//     * 매칭 상태를 외부 시스템으로 업데이트하는 메서드.
//     * - 매칭 상태 업데이트 요청을 POST 방식으로 전송합니다.
//     * - 성공 및 실패 응답을 로그로 출력합니다.
//     */
//    public void updateMatchStatus(Long matchId, String status) {
//        logger.info("매칭 상태 업데이트 요청 시작: matchId={}, status={}", matchId, status);
//
//        webClient.post()
//                .uri("/update-match-status")
//                .bodyValue(new MatchStatusUpdateRequest(matchId, status))
//                .retrieve()
//                .bodyToMono(String.class)
//                .doOnSuccess(response -> logger.info("매칭 상태 업데이트 전송 성공: matchId={}, response={}", matchId, response))
//                .doOnError(error -> logger.error("매칭 상태 업데이트 전송 실패: matchId={}, error={}", matchId, error.getMessage()))
//                .subscribe();
//    }
//}
