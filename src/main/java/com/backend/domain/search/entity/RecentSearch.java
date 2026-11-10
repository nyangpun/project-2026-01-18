package com.backend.domain.search.entity;

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
public class RecentSearch extends BaseEntity {
    private Long userId;
    private String searchType;
    private String keyword;
}
