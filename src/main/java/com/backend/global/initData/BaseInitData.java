package com.backend.global.initData;

import com.backend.domain.memeber.service.MemberService;
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

import com.backend.domain.post.post.service.PostService;

@Configuration
public class BaseInitData {
  private final PostService postService;
  private final ReviewService reviewService;

  @Autowired
  @Lazy
  private BaseInitData self;

  public BaseInitData(PostService postService, ReviewService reviewService) {
    this.postService = postService;
    this.reviewService = reviewService;
  }

  @Bean
  public ApplicationRunner baseInitDataApplicationRunner() {
    return args -> {
      self.work1();
      self.work2();
    };
  }

  @Transactional
  public void work1() {
    if (postService.count() > 0)
      return;

    postService.write("제목 1", "내용 1");
    postService.write("제목 2", "내용 2");
  }

  @Transactional
  public void work2() {
    if (reviewService.count() > 0)
      return;

    reviewService.writeReview("내용1", 5);
    reviewService.writeReview("내용2", 3);
  }


}
