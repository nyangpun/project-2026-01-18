package com.backend.domain.game.dto;

import com.backend.domain.game.entity.Game;

public record GameResponseDto(Long id, long appid, String name, String developer, String publisher, String tag) {
    public static GameResponseDto from(Game game) {
        return new GameResponseDto(game.getId(), game.getAppid(), game.getName(), game.getDeveloper(), game.getPublisher(), game.getTag());
    }
}
