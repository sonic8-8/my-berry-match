package com.gongcha.berrymatch.game;

/**
 * 경기 진행 상태
 */
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameStatus {
    COMPLETED("경기 완료"),
    RECORDING_COMPLETED("경기 기록까지 완료");

    private final String text;
}
