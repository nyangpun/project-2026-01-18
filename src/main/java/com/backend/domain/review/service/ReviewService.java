package com.backend.domain.review.service;

import com.backend.domain.review.entity.Review;
import com.backend.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    public List<Review> findAll(){return reviewRepository.findAll();}
    public Long count() {
        return reviewRepository.count();
    }

    public void writeReview(String content, int score){
        Review review = Review.builder()
                .content(content)
                .score(score)
                .build();
        reviewRepository.save(review);
    }
    public Review findById(long id){return reviewRepository.findById(id).orElse(null);}
}
