package com.backend.domain.review.service;

import com.backend.domain.review.entity.Review;
import com.backend.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public void writeReview(String content, int score, Long gameId){
        Review review = Review.builder()
                .content(content)
                .score(score)
                .gameId(gameId)
                .build();
        reviewRepository.save(review);
    }

    public Review findById(long id){return reviewRepository.findById(id).orElse(null);}

    public List<Review> findByGameId(long gameId) {return reviewRepository.findByGameId(gameId);}

    public Page<Review> getReviewWithPaging(Long gameId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reviewRepository.findByGameId(gameId, pageable);
    }

}
