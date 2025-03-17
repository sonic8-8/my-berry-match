package com.gongcha.berrymatch.user.requestDTO;

import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import com.gongcha.berrymatch.user.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UserSignupServiceRequest {

    private String identifier;
    private ProviderInfo providerInfo;
    private String nickname;
    private City city;
    private District district;
    private Gender gender;
    private LocalDate birthdate;
    private String phoneNumber;
    private String profileImageUrl;
    private String introduction;

    @Builder
    public UserSignupServiceRequest(String identifier, ProviderInfo providerInfo,String nickname, City city, District district, Gender gender, LocalDate birthdate, String phoneNumber, String profileImageUrl, String introduction) {
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

    public User toEntity() {
        return User.builder()
                .identifier(identifier)
                .providerInfo(providerInfo)
                .nickname(nickname)
                .city(city)
                .district(district)
                .gender(gender)
                .birthDate(birthdate)
                .phoneNumber(phoneNumber)
                .profileImageUrl(profileImageUrl)
                .introduction(introduction)
                .build();
    }
}
