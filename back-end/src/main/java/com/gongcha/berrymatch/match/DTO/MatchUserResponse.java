package com.gongcha.berrymatch.match.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchUserResponse {
    private Long id; // 유저 ID
    private String nickname; // 유저 닉네임
    private String profileImageUrl; // 유저 프로필 사진 URL
    private String team; // 유저의 팀 (A팀, B팀 등)
    private String readyState;
}
