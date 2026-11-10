package com.backend.domain.search.repository;

import com.backend.domain.search.entity.RecentSearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecentSearchRepository extends JpaRepository<RecentSearch, Long> {
    void deleteByUserIdAndSearchTypeAndKeyword(Long userId, String searchType, String keyword);
    List<RecentSearch> findByUserIdOrderByIdDesc(Long userId);
    List<RecentSearch> findTop10ByUserIdOrderByIdDesc(Long userId);
}
