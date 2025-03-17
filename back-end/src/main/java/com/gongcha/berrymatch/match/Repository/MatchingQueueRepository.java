package com.gongcha.berrymatch.match.Repository;

import com.gongcha.berrymatch.match.domain.MatchQueueStatus;
import com.gongcha.berrymatch.match.domain.MatchingQueue;
import com.gongcha.berrymatch.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchingQueueRepository extends JpaRepository<MatchingQueue, Long> {


    /**
     * @param status 매칭대기열 유저상태
     * @return  매칭대기중인 유저 항목
     */
    @Query("SELECT mq FROM MatchingQueue mq WHERE mq.status = :status AND mq.groupCode IS NULL ORDER BY mq.enqueuedAt ASC")
    List<MatchingQueue> findByStatusAndGroupCodeIsNullOrderByEnqueuedAtAsc(@Param("status") MatchQueueStatus status);


    /**
     * 특정 유저 ID로 매칭 큐 항목을 조회합니다.
     *
     * @param userId 유저 ID
     * @return 매칭 큐 항목
     */
    Optional<MatchingQueue> findByUserId(Long userId);


    /**
     * @param status 매칭대기열 유저상태
     * @return  매칭 완료된 유저 항목
     */
    List<MatchingQueue> findByStatus(MatchQueueStatus status);


    Optional<MatchingQueue> findByUserAndStatus(User user, MatchQueueStatus matchQueueStatus);
}
