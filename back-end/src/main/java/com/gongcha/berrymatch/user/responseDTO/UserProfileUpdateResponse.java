package com.gongcha.berrymatch.user.responseDTO;

import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfileUpdateResponse {

    private Long id;
    private String identifier;
    private ProviderInfo providerInfo;
    private String profileImageUrl;
    private String introduction;

    @Builder
    public UserProfileUpdateResponse(Long id, String identifier, ProviderInfo providerInfo, String profileImageUrl, String introduction) {
        this.id = id;
        this.identifier = identifier;
        this.providerInfo = providerInfo;
        this.profileImageUrl = profileImageUrl;
        this.introduction = introduction;
    }
}
