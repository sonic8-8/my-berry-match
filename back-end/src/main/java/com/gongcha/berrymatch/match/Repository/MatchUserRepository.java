package com.gongcha.berrymatch.match.Repository;

import com.gongcha.berrymatch.match.domain.Match;
import com.gongcha.berrymatch.match.domain.MatchStatus;
import com.gongcha.berrymatch.match.domain.MatchUser;
import com.gongcha.berrymatch.match.domain.MatchUserReady;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchUserRepository extends JpaRepository<MatchUser, Long> {


    // 모든 매칭된 유저들 가져오기
    List<MatchUser> findAll();


    Optional<MatchUser> findByUserId(Long userId);

    List<MatchUser> findAllByUserId(Long userId);


    boolean existsByMatch(Match match);

    // 요청한 유저가 속한 경기 전 또는 경기 중 상태의 매칭 정보 조회
    List<MatchUser> findByUser_IdAndMatch_MatchStatusIn(Long userId, List<MatchStatus> statuses);

    /**
     * @param matchId 메치테이블 식별자
     * @param status 매치유저테이블 준비 상태
     * @return 매치유저테이블 준비완료/ 대기중 상태 항목
     */
    List<MatchUser> findByMatchIdAndStatus(Long matchId, MatchUserReady status);

    List<MatchUser> findAllByMatchId(Long matchId);

//    @Query("select u from User u where u.identifier = :identifier and u.role = :role")
//    @Query("select u from MatchUser u where u.")
//    Optional<MatchUser> findByNickname(@Param("nickname") String nickname);
//

    @Query("SELECT m FROM Match m JOIN m.matchUsers mu WHERE mu IN :matchUsers")
    Match findByMatchUsers(List<MatchUser> matchUsers);

}
