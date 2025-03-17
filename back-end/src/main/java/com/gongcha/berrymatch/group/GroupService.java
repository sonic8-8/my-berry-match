package com.gongcha.berrymatch.group;

import com.gongcha.berrymatch.user.User;
import com.gongcha.berrymatch.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public GroupResponse createGroup(GroupRequest groupRequest) {
        // 유저 찾기
        User user = userRepository.findById(groupRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 사용자가 이미 그룹에 속해 있는지 확인
        if (user.getUserGroup() != null) {
            throw new IllegalStateException("User has already created or joined a group");
        }

        // 8자리 랜덤 코드 생성
        String groupCode = generateGroupCode();

        // 그룹 엔티티 생성
        UserGroup userGroup = UserGroup.builder()
                .maxMembers(2)
                .groupCreatedAt(Timestamp.from(Instant.now()))
                .groupCode(groupCode)
                .leader(user)  // 생성자를 리더로 설정
                .members(new ArrayList<>())  // List<User>로 초기화
                .build();

        // 그룹에 생성자를 멤버로 추가
        userGroup.addMember(user);

        // 사용자의 그룹 설정
        user.setUserGroup(userGroup);

        // 그룹 저장
        UserGroup savedGroup = groupRepository.save(userGroup);

        return GroupResponse.fromUserGroup(savedGroup);
    }

    @Transactional
    public GroupResponse joinGroup(GroupRequest groupRequest) {
        // 유저 찾기
        User user = userRepository.findById(groupRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 사용자가 이미 다른 그룹에 속해 있는지 확인
        if (user.getUserGroup() != null) {
            throw new IllegalStateException("User is already a member of another group");
        }

        // 그룹 코드로 그룹 조회
        UserGroup userGroup = groupRepository.findByGroupCode(groupRequest.getGroupCode())
                .orElseThrow(() -> new IllegalArgumentException("Group not found with code: " + groupRequest.getGroupCode()));

        // 그룹에 사용자 추가
        userGroup.addMember(user);

        // 사용자의 그룹 설정
        user.setUserGroup(userGroup);

        // 그룹 업데이트
        UserGroup updatedGroup = groupRepository.save(userGroup);

        return GroupResponse.fromUserGroup(updatedGroup);
    }

    @Transactional
    public GroupResponse leaveGroup(GroupRequest groupRequest) {
        User user = userRepository.findById(groupRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserGroup userGroup = user.getUserGroup();
        if (userGroup == null) {
            throw new IllegalStateException("User is not a member of any group");
        }

        boolean isLeader = user.equals(userGroup.getLeader());

        userGroup.removeMember(user); // 그룹에서 사용자를 제거
        userRepository.save(user); // 변경된 사용자 정보를 저장

        GroupResponse groupResponse;
        if (userGroup.getMembers().isEmpty()) {
            groupRepository.delete(userGroup);
            return GroupResponse.builder()
                    .groupId(null)
                    .groupCode(userGroup.getGroupCode())
                    .maxMembers(userGroup.getMaxMembers())
                    .groupCreatedAt(userGroup.getGroupCreatedAt())
                    .members(new ArrayList<>())
                    .build();
        } else {
            if (isLeader) {
                // 현재 사용자가 그룹장인 경우, 다음 멤버를 그룹장으로 설정
                User newLeader = userGroup.getMembers().get(0);
                userGroup.setLeader(newLeader);
            }

            UserGroup updatedGroup = groupRepository.save(userGroup);
            groupResponse = GroupResponse.fromUserGroup(updatedGroup);

            // Node.js 서버에 그룹 나가기 정보 전송
//            sendToSubServer(userGroup.getGroupCode(), groupResponse);

            return groupResponse;
        }
    }


    public GroupResponse getGroupByUserId(Long id) {
        UserGroup userGroup = groupRepository.findByUserId(id)
                .orElseThrow(() -> new IllegalArgumentException("Group not found for user with id: " + id));

        return GroupResponse.fromUserGroup(userGroup);
    }


    private String generateGroupCode() {
        return UUID.randomUUID().toString().substring(0, 8);  // UUID로부터 8자리 랜덤 코드 생성
    }
}

