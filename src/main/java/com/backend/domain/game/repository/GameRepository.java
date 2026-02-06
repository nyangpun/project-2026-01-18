package com.backend.domain.game.repository;

import com.backend.domain.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    boolean existsByAppid(long appid);
}
