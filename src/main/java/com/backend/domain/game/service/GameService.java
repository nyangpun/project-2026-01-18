package com.backend.domain.game.service;

import com.backend.domain.game.dto.gamedto;
import com.backend.domain.game.entity.Game;
import com.backend.domain.game.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final WebClient webClient;
    private static final String Url = "https://steamspy.com/api.php?request=all";

    @Transactional
    public void syncAllFromSteamSpy() {
        Map<String, gamedto> map = webClient.get()
                .uri(Url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, gamedto>>() {
                })
                .block();

        if (map == null || map.isEmpty()) {
            return;
        }

        List<Game> toSave = map.values().stream()
                .filter(gamedto -> !gameRepository.existsByAppid(gamedto.getAppid()))
                .map(this::toEntity)
                .collect(Collectors.toList());

        gameRepository.saveAll(toSave);
    }

    private Game toEntity(gamedto dto) {
        String tagString = null;
        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            tagString = dto.getTags().keySet().stream()
                    .sorted()
                    .collect(Collectors.joining(","));
        }

        return Game.builder()
                .appid(dto.getAppid())
                .name(dto.getName())
                .developer(dto.getDeveloper())
                .publisher(dto.getPublisher())
                .tag(tagString)
                .build();
    }
}
