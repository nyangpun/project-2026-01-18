package com.backend.global.initData;

import com.backend.domain.member.service.MemberService;
import com.backend.domain.review.entity.Review;
import com.backend.domain.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class BaseInitData {
  private final ReviewService reviewService;

  @Autowired
  @Lazy
  private BaseInitData self;

  public BaseInitData(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @Bean
  public ApplicationRunner baseInitDataApplicationRunner() {
    return args -> {
      self.work2();
    };
  }

  @Transactional
  public void work2() {
    if (reviewService.count() > 0)
      return;

    reviewService.writeReview("내용1", 5, 1L, null);
    reviewService.writeReview("내용2", 3, 2L, null);
    reviewService.writeReview("내용3", 1, 1L, null);
  }
}
