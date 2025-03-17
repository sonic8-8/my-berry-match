package com.gongcha.berrymatch.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameEndVoteStatus {

    YES("경기 종료 동의"),
    NO("경기 종료 비동의");

    private final String text;
}
