package com.gongcha.berrymatch.match.Service;

import com.gongcha.berrymatch.match.DTO.MatchUserRequest;
import com.gongcha.berrymatch.match.DTO.MatchUserResponse;
import com.gongcha.berrymatch.match.Repository.MatchUserRepository;
import com.gongcha.berrymatch.match.domain.MatchUser;
import com.gongcha.berrymatch.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetMatchUserService {

    private final MatchUserRepository matchUserRepository;

    public GetMatchUserService(MatchUserRepository matchUserRepository) {
        this.matchUserRepository = matchUserRepository;
    }

    // 매치 유저 정보를 조회하여 리스트로 반환하는 메소드
    public List<MatchUserResponse> getMatchUser(MatchUserRequest matchUserRequest) {
        // 1. 요청자의 유저 ID로 매치 유저 정보 조회
        List<MatchUser> userMatches = matchUserRepository.findAllByUserId(matchUserRequest.getId());

        // 2. 해당 유저가 속한 매치가 없으면 예외 처리
        if (userMatches == null || userMatches.isEmpty()) {
            throw new RuntimeException("해당 유저가 속한 매치를 찾을 수 없습니다.");
        }

        // 3. 해당 유저가 속한 매치 ID를 기준으로 모든 매치 유저를 조회
        Long matchId = userMatches.get(0).getMatch().getId(); // 매치 ID 추출
        List<MatchUser> matchUsers = matchUserRepository.findAllByMatchId(matchId);

        // 4. 매치에 속한 유저들의 정보를 MatchUserResponse로 변환하여 리스트로 반환
        return matchUsers.stream().map(matchUser -> {
            User user = matchUser.getUser();

            if (user == null) {
                throw new RuntimeException("유저 정보를 찾을 수 없습니다.");
            }

            String readyState = matchUser.getStatus().name();

            return new MatchUserResponse(
                    user.getId(),
                    user.getNickname(),
                    user.getProfileImageUrl(),
                    matchUser.getTeam().name(),
                    readyState
            );

        }).collect(Collectors.toList()); // 리스트로 변환하여 반환
    }
}
