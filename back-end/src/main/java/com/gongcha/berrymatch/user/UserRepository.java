package com.gongcha.berrymatch.user;

import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdentifier(String identifier);

    @Query("select u from User u where u.identifier = :identifier")
    List<User> findAllByIdentifier(String identifier);

    Optional<User> findByNickname(String nickname);

    @Query("select u from User u where u.identifier = :identifier and u.providerInfo = :providerInfo")
    Optional<User> findByOAuthInfo(@Param("identifier") String identifier,
                                   @Param("providerInfo") ProviderInfo providerInfo);

    @Query("select u from User u where u.identifier = :identifier and u.role = :role")
    Optional<User> findByIdentifierAndRole(@Param("identifier") String identifier,
                                           @Param("role") Role role);

    @Transactional
    @Modifying
    @Query("delete from User u where u.nickname = :nickname")
    void deleteByNickname(@Param("nickname") String nickname);

}
