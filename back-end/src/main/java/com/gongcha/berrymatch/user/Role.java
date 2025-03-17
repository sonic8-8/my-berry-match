package com.gongcha.berrymatch.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    NOT_REGISTERED("ROLE_NOT_REGISTERED","비회원"),
    USER("ROLE_USER","일반 사용자"),
    ADMIN("ROLE_ADMIN","관리자");

    private final String key;
    private final String text;

}
