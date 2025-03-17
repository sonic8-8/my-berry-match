package com.gongcha.berrymatch.userActivity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogType {

    LOGIN("로그인"),
    LOGOUT("로그아웃");

    private final String text;

}
