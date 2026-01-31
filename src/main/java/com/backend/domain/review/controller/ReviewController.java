package com.backend.domain.review.controller;

import com.backend.domain.review.entity.Review;
import com.backend.domain.review.form.ReviewForm;
import com.backend.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/list")
    public String getReviews(Model model){
        List<Review> reviews = reviewService.findAll();
        model.addAttribute("review", reviews);

        return "review/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable long id, Model model){
        Review review = reviewService.findById(id);
        model.addAttribute("review", review);
        return "review/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String showWrite(Model model){
        model.addAttribute("reviewForm", new ReviewForm());
        return "review/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(ReviewForm review){
        reviewService.writeReview(review.getContent(), review.getScore());
        return "redirect:/review/list";
    }
}
