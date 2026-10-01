package com.backend.domain.review.dto;

import com.backend.domain.review.entity.Review;

public record ReviewDetailDto(Long id, String content, int score) {
    public static ReviewDetailDto from(Review review) {
        return new ReviewDetailDto(review.getId(), review.getContent(), review.getScore());
    }
}
