package com.gongcha.berrymatch.match.domain;

import lombok.Getter;


/**
 * 매칭요청 종목
 * 축구
 * 풋살
 * 인원수 설정가능
 */
@Getter
public enum Sport {

    FOOTBALL(12),
    SOCCER(12);

    private final int maxSize;

    Sport(int maxSize) {
        this.maxSize = maxSize;
    }

}
