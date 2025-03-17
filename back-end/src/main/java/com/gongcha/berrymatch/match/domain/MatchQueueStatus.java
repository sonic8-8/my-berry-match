package com.gongcha.berrymatch.match.domain;


/**
 * 매칭대기열테이블 유저 상태
 */
public enum MatchQueueStatus {
    PENDING,    // 매칭 대기중
    MATCHED,   // 매칭완료
    CANCELLED// 매칭 취소
}