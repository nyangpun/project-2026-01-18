package com.backend.domain.review.controller;

import com.backend.domain.game.entity.Game;
import com.backend.domain.game.repository.GameRepository;
import com.backend.domain.game.service.GameService;
import com.backend.domain.review.entity.Review;
import com.backend.domain.review.form.ReviewForm;
import com.backend.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final GameRepository gameRepository;

    @GetMapping("/list")
    public String getReviews(Model model){
        List<Review> reviews = reviewService.findAll();
        List<Game> games = gameRepository.findAll();
        model.addAttribute("review", reviews);
        model.addAttribute("game", games);

        return "review/list";
    }

    @GetMapping("/game/{gameId}")
    public String listReviewsByGame(@PathVariable Long gameId, Model model) {
        Game game = gameRepository.findById(gameId).orElse(null);

        List<Review> reviews = reviewService.findByGameId(gameId);

        model.addAttribute("game", game);
        model.addAttribute("reviews", reviews);

        return "review/gameReviews";
    }


    @GetMapping("/{id}")
    public String detail(@PathVariable long id, Model model){
        Review review = reviewService.findById(id);
        model.addAttribute("review", review);
        return "review/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String showWrite(@RequestParam Long gameId, Model model){
        ReviewForm reviewForm = new ReviewForm();
        reviewForm.setGameId(gameId);
        model.addAttribute("reviewForm", reviewForm);
        return "review/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(ReviewForm review){
        reviewService.writeReview(review.getContent(), review.getScore(), review.getGameId());
        return "redirect:/review/game/" + review.getGameId();
    }
}