package com.backend.domain.game.repository;

import com.backend.domain.game.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    boolean existsByAppid(long appid);

    List<Game> findByTagIsNull();

    Page<Game> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Game> findByTagContainingIgnoreCase(String tag, Pageable pageable);
    Page<Game> findByDeveloperContainingIgnoreCase(String developer, Pageable pageable);
    Page<Game> findByPublisherContainingIgnoreCase(String publisher, Pageable pageable);
}
