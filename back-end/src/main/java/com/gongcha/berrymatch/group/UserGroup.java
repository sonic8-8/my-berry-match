package com.gongcha.berrymatch.group;

import com.gongcha.berrymatch.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user_group")
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int maxMembers;
    private Timestamp groupCreatedAt;

    @Column(length = 8)
    private String groupCode;

    @OneToMany(mappedBy = "userGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 12)
    private List<User> members = new ArrayList<>();

    @ManyToOne // 리더는 여러 그룹을 이끌 수 있지만, 한 그룹에 한 명의 리더만 존재
    @JoinColumn(name = "leader_id")
    private User leader;

    public boolean addMember(User user) {
        if (members.size() >= maxMembers) {
            throw new IllegalStateException("Group is full");
        }
        user.setUserGroup(this);
        return members.add(user);
    }

    public boolean removeMember(User user) {
        user.setUserGroup(null);
        return members.remove(user);
    }
}
