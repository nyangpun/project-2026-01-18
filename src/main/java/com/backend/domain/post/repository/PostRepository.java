package com.backend.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.domain.post.entity.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
  List<Post> findByBoardId(Long boardId);
}
