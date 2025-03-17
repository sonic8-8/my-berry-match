package com.gongcha.berrymatch.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum City {

    SEOUL("서울"),
    BUSAN("부산"),
    DAEGU("대구"),
    INCHEON("인천"),
    GWANGJU("광주"),
    DAEJEON("대전"),
    ULSAN("울산"),
    SEJONG("세종");

    private final String text;

    @JsonCreator
    public static City fromValue(String value) {
        for (City city : City.values()) {
            // 영문 또는 한글 텍스트 모두 지원
            if (city.name().equalsIgnoreCase(value) || city.text.equalsIgnoreCase(value)) {
                return city;
            }
        }
        throw new IllegalArgumentException("Unknown city: " + value);
    }

    @JsonValue
    public String getText() {
        return text;
    }

}
