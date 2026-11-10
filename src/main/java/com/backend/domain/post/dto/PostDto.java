package com.backend.domain.post.dto;

import com.backend.domain.post.entity.Post;

public record PostDto(Long id, String title, String content, Long boardId, Long userId) {
    public static PostDto from(Post post) {
        return new PostDto(post.getId(), post.getTitle(), post.getContent(), post.getBoardId(), post.getUserId());
    }
}
