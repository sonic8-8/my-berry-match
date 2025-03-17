package com.gongcha.berrymatch.user.requestDTO;

import com.gongcha.berrymatch.user.City;
import com.gongcha.berrymatch.user.District;
import com.gongcha.berrymatch.user.UserMatchStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DummyAddRequest {

    private String id;
    private String nickname;
    private String userMatchStatus;
    private String city;
    private String district;

    @Builder
    public DummyAddRequest(String id, String nickname, String userMatchStatus, String city, String district) {
        this.id = id;
        this.nickname = nickname;
        this.userMatchStatus = userMatchStatus;
        this.city = city;
        this.district = district;
    }

    public DummyAddServiceRequest toServiceRequest() {
        return DummyAddServiceRequest.builder()
                .id(Long.valueOf(id))
                .nickname(nickname)
                .userMatchStatus(UserMatchStatus.valueOf(userMatchStatus))
                .city(City.valueOf(city))
                .district(District.valueOf(district))
                .build();
    }
}
