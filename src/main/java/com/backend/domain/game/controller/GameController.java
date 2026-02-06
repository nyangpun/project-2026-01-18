package com.backend.domain.game.controller;

import com.backend.domain.game.dto.gamedto;
import com.backend.domain.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    @GetMapping("/sync")
    public String syncFromSteamSpy() {
        gameService.syncAllFromSteamSpy();
        return "redirect:/"; // 원하는 페이지로 리다이렉트
    }

}
