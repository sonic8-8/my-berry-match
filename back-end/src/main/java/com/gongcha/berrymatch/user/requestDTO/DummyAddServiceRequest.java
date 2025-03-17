package com.gongcha.berrymatch.user.requestDTO;

import com.gongcha.berrymatch.user.City;
import com.gongcha.berrymatch.user.District;
import com.gongcha.berrymatch.user.UserMatchStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DummyAddServiceRequest {
    private Long id;
    private String nickname;
    private UserMatchStatus userMatchStatus;
    private City city;
    private District district;

    @Builder
    public DummyAddServiceRequest(Long id, String nickname, UserMatchStatus userMatchStatus, City city, District district) {
        this.id = id;
        this.nickname = nickname;
        this.userMatchStatus = userMatchStatus;
        this.city = city;
        this.district = district;
    }
}