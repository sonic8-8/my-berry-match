package com.gongcha.berrymatch.user.requestDTO;

import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import com.gongcha.berrymatch.user.City;
import com.gongcha.berrymatch.user.District;
import com.gongcha.berrymatch.user.Gender;
import com.gongcha.berrymatch.user.UserMatchStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;



@Getter
@NoArgsConstructor
public class UserSignupRequest {

    private String identifier;
    private String providerInfo;
    private String nickname;
    private City city;
    private District district;
    private Gender gender;
    private LocalDate birthdate;
    private String phoneNumber;
    private String profileImageUrl;
    private String introduction;


    @Builder
    public UserSignupRequest(String identifier,String providerInfo, String nickname, City city, District district, Gender gender, LocalDate birthdate, String phoneNumber, String profileImageUrl, String introduction) {
        this.identifier = identifier;
        this.providerInfo = providerInfo;
        this.nickname = nickname;
        this.city = city;
        this.district = district;
        this.gender = gender;
        this.birthdate = birthdate;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.introduction = introduction;

    }

    public ProviderInfo toProviderInfo(String providerInfo) {
        return ProviderInfo.valueOf(providerInfo);
    }

    public UserSignupServiceRequest toService() {
        return UserSignupServiceRequest.builder()
                .identifier(identifier)
                .providerInfo(ProviderInfo.from(providerInfo))
                .nickname(nickname)
                .city(city)
                .district(district)
                .gender(gender)
                .birthdate(birthdate)
                .phoneNumber(phoneNumber)
                .profileImageUrl(profileImageUrl)
                .introduction(introduction)
                .build();
    }
}
