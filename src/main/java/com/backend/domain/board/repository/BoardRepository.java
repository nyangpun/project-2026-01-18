package com.backend.domain.board.repository;

import com.backend.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    boolean existsByGameId(Long gameId);
    Optional<Board> findByGameId(Long gameId);
}
