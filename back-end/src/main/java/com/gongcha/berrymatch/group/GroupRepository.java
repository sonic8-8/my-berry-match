package com.gongcha.berrymatch.group;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository  extends JpaRepository<UserGroup,Long> {

    Optional<UserGroup> findByGroupCode(String groupCode);

    // 유저 ID로 해당 유저가 속한 그룹을 조회하는 쿼리
    @Query("SELECT ug FROM UserGroup ug JOIN ug.members u WHERE u.id = :userId")
    Optional<UserGroup> findByUserId(@Param("userId") Long id);




}
