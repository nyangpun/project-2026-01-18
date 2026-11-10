package com.backend.domain.search.dto;

import com.backend.domain.search.entity.RecentSearch;

public record RecentSearchDto(String searchType, String keyword) {
    public static RecentSearchDto from(RecentSearch recentSearch) {
        return new RecentSearchDto(recentSearch.getSearchType(), recentSearch.getKeyword());
    }
}
