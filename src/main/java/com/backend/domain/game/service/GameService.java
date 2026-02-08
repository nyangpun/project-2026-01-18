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
    private static final String Url = "https://steamspy.com/api.php?request=all&languages=Korean";
    private static final String Url2 = "https://steamspy.com/api.php?request=appdetails&appid={appid}";

    @Transactional
    public void syncAllFromSteamSpy() {
        Map<String, gamedto> map = webClient.get()
                .uri(Url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, gamedto>>() {})
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

        return Game.builder()
                .appid(dto.getAppid())
                .name(dto.getName())
                .developer(dto.getDeveloper())
                .publisher(dto.getPublisher())
                .tag(tagString)
                .build();
    }

    @Transactional
    public void filltags() {
        List<Game> gameTagisNull = gameRepository.findByTagIsNull();

        for(Game game : gameTagisNull) {
            String tagString = findtags(game.getAppid());
            if(tagString == null) {
                continue;
            }
            game.setTag(tagString);
        }
    }

    private String findtags(long appid) {
        gamedto dto = webClient.get()
                .uri(Url2, appid)
                .retrieve()
                .bodyToMono(gamedto.class)
                .block();

        return dto.getTags().keySet().stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .sorted()
                .collect(Collectors.joining(","));
    }

    public List<Game> getAllgames() {
        return gameRepository.findAll();
    }
}
