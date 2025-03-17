package com.gongcha.berrymatch.chatMessage;

import com.gongcha.berrymatch.match.domain.Match;
import com.gongcha.berrymatch.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository <ChatMessage,Long> {

    List<ChatMessage> findByMatchIdOrderByCreatedAtAsc(Long matchId);

    @Query("delete from ChatMessage cm where cm.user = :user")
    void deleteByUser(@Param("user") User user);
}
