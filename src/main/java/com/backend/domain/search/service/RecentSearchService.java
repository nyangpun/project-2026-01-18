package com.backend.domain.search.service;

import com.backend.domain.search.dto.RecentSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentSearchService {
    private final StringRedisTemplate redisTemplate;

    public void save(Long userId, String searchType, String keyword) {
        String key = "recent-search:" + userId;
        String value = (searchType == null ? "" : searchType) + ":" + keyword;

        redisTemplate.opsForList().remove(key, 0, value);
        redisTemplate.opsForList().leftPush(key, value);
        redisTemplate.opsForList().trim(key, 0, 9);
    }

    public List<RecentSearchDto> findRecent(Long userId) {
        List<String> values = redisTemplate.opsForList().range("recent-search:" + userId, 0, 9);
        return values.stream().map(value -> {
            String[] parts = value.split(":", 2);
            return new RecentSearchDto(parts[0].isEmpty() ? null : parts[0], parts[1]);
        }).toList();
    }
}
