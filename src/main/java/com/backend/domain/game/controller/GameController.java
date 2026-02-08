package com.backend.domain.game.controller;

import com.backend.domain.game.dto.gamedto;
import com.backend.domain.game.entity.Game;
import com.backend.domain.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    @GetMapping("/sync")
    public String syncFromSteamSpy() {
        gameService.syncAllFromSteamSpy();
        return "redirect:/";
    }
    @GetMapping("/filltags")
    public String filltags() {
        gameService.filltags();
        return "redirect:/";
    }

    //임시
    @GetMapping("/list")
    @ResponseBody
    public List<Game> getAllGames(){
        return gameService.getAllgames();
    }
}
