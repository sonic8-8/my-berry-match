package com.gongcha.berrymatch.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameRepository extends JpaRepository<Game,Long> {

    @Query("SELECT g.gameStatus FROM Game g WHERE g.id = :gameId")
    GameStatus findGameStatusById(@Param("gameId") Long gameId);

    Game findAllById(@Param("id") Long gameId);

    @Query("SELECT g FROM Game g JOIN g.users u WHERE u.id = :userId")
    List<Game> findAllByUserId(@Param("userId") Long userId);

}
