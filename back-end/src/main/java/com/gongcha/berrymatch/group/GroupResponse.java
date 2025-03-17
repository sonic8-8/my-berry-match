package com.gongcha.berrymatch.group;

import com.gongcha.berrymatch.user.User;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GroupResponse {

    private Long groupId;
    private String groupCode;
    private int maxMembers;
    private Timestamp groupCreatedAt;
    private List<User> members;
    private User leader;  // 리더 필드 추가

    public static GroupResponse fromUserGroup(UserGroup userGroup) {
        return GroupResponse.builder()
                .groupId(userGroup.getId())
                .groupCode(userGroup.getGroupCode())
                .maxMembers(userGroup.getMaxMembers())
                .groupCreatedAt(userGroup.getGroupCreatedAt())
                .members(userGroup.getMembers())
                .leader(userGroup.getLeader())  // 리더 정보 설정
                .build();
    }
}
