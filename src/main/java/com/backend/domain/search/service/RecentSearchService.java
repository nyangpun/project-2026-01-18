package com.backend.domain.search.service;

import com.backend.domain.search.entity.RecentSearch;
import com.backend.domain.search.repository.RecentSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentSearchService {
    private final RecentSearchRepository recentSearchRepository;

    @Transactional
    public void save(Long userId, String searchType, String keyword) {
        recentSearchRepository.deleteByUserIdAndSearchTypeAndKeyword(userId, searchType, keyword);

        RecentSearch recentSearch = RecentSearch.builder()
                .userId(userId)
                .searchType(searchType)
                .keyword(keyword)
                .build();
        recentSearchRepository.save(recentSearch);

        List<RecentSearch> list = recentSearchRepository.findByUserIdOrderByIdDesc(userId);
        if (list.size() > 10) {
            recentSearchRepository.deleteAll(list.subList(10, list.size()));
        }
    }

    public List<RecentSearch> findRecent(Long userId) {
        return recentSearchRepository.findTop10ByUserIdOrderByIdDesc(userId);
    }
}
