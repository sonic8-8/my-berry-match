package com.gongcha.berrymatch.user.responseDTO;

import com.gongcha.berrymatch.user.City;
import com.gongcha.berrymatch.user.District;
import com.gongcha.berrymatch.user.UserMatchStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserInfoResponse {
    private String identifier;

    private String nickname;

    private City city;

    private District district;

    private String profileImageUrl;

    private String introduction;

    private UserMatchStatus userMatchStatus;

    @Builder
    public UserInfoResponse(String identifier, String nickname, City city, District district, String profileImageUrl, String introduction, UserMatchStatus userMatchStatus) {
        this.identifier = identifier;
        this.nickname = nickname;
        this.city = city;
        this.district = district;
        this.profileImageUrl = profileImageUrl;
        this.introduction = introduction;
        this.userMatchStatus = userMatchStatus;
    }
}
