package com.backend.domain.member.entity;

import com.backend.global.base.config.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerification extends BaseEntity {
    @Column(unique = true)
    private String token;
    private Long memberId;
    private LocalDateTime expiresAt;
}
