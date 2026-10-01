package com.backend.domain.review.controller;

import com.backend.domain.game.entity.Game;
import com.backend.domain.game.repository.GameRepository;
import com.backend.domain.game.service.GameService;
import com.backend.domain.review.dto.ReviewDetailDto;
import com.backend.domain.review.entity.Review;
import com.backend.domain.review.form.ReviewForm;
import com.backend.domain.review.service.ReviewService;
import com.backend.global.security.dto.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final GameService gameService;
    private final GameRepository gameRepository;

    @GetMapping("/list")
    public String getReviews(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "25") int size){
        Page<Game> gamePage = this.gameService.getGamePage(page -1, size);
        model.addAttribute("gamePage", gamePage);

        return "review/list";
    }

    @GetMapping("/game/{gameId}")
    public String listReviewsByGame(@PathVariable Long gameId, Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "25") int size) {
        Game game = gameRepository.findById(gameId).orElse(null);
        Page<Review> reviewPage = this.reviewService.getReviewWithPaging(gameId,page -1, size);

        model.addAttribute("game", game);
        model.addAttribute("reviewPage", reviewPage);

        return "review/gameReviews";
    }


    @GetMapping("/{id}")
    public String detail(@PathVariable long id, Model model){
        Review review = reviewService.findById(id);
        model.addAttribute("review", ReviewDetailDto.from(review));
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
    public String write(ReviewForm review, @AuthenticationPrincipal MemberContext memberContext){
        reviewService.writeReview(review.getContent(), review.getScore(), review.getGameId(), memberContext.getId());
        return "redirect:/review/game/" + review.getGameId();
    }
}