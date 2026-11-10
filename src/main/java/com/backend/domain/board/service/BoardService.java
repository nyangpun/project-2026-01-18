package com.backend.domain.board.service;

import com.backend.domain.board.entity.Board;
import com.backend.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Board create(Long gameId, String title) {
        if (boardRepository.existsByGameId(gameId)) {
            return boardRepository.findByGameId(gameId).get();
        }

        Board board = Board.builder()
                .gameId(gameId)
                .title(title)
                .build();

        return boardRepository.save(board);
    }

    public List<Board> findAll() {return boardRepository.findAll();}

    public Board findById(long id) {return boardRepository.findById(id).orElse(null);}
}
