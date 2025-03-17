package com.gongcha.berrymatch.user.responseDTO;

import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import com.gongcha.berrymatch.user.Role;
import com.gongcha.berrymatch.user.UserMatchStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignupResponse {
    private String identifier;
    private Role role;
    private String providerInfo;
    private UserMatchStatus userMatchStatus;

    @Builder
    public UserSignupResponse(String identifier, Role role, ProviderInfo providerInfo, UserMatchStatus userMatchStatus) {
        this.identifier = identifier;
        this.role = role;
        this.providerInfo = ProviderInfo.toString(providerInfo);
        this.userMatchStatus = userMatchStatus;
    }

}
