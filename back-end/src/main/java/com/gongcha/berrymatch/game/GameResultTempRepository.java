package com.gongcha.berrymatch.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameResultTempRepository extends JpaRepository<GameResultTemp,Long> {

    List<GameResultTemp> findDistinctResultsByGameId(@Param("gameId") Long gameId);

    List<GameResultTemp> findAllByGameId(@Param("gameId") Long gameId);
}
