package com.backend.domain.board.entity;

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
public class Board extends BaseEntity {
    private Long gameId;
    private String title;
}
