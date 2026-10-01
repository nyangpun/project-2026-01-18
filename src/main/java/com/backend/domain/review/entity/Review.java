package com.backend.domain.review.entity;

import com.backend.global.base.config.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class Review extends BaseEntity {
    private String content;
    private Long userId;
    private int score;
    private Long gameId;
}
