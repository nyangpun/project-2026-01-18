package com.backend.domain.board.dto;

import com.backend.domain.board.entity.Board;

public record BoardDto(Long id, Long gameId, String title) {
    public static BoardDto from(Board board) {
        return new BoardDto(board.getId(), board.getGameId(), board.getTitle());
    }
}
