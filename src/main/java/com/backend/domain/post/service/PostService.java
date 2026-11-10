package com.backend.domain.post.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.backend.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import com.backend.domain.post.entity.Post;

@Service
@RequiredArgsConstructor
public class PostService {
  private final PostRepository postRepository;

  public List<Post> findByBoardId(Long boardId) {
    return postRepository.findByBoardId(boardId);
  }

  public void write(String title, String content, Long boardId, Long userId) {
    Post post = Post.builder()
        .title(title)
        .content(content)
        .boardId(boardId)
        .userId(userId)
        .build();

    postRepository.save(post);
  }

  public void modify(Post post, String title, String content) {
    post.setTitle(title);
    post.setContent(content);
    postRepository.save(post);
  }

  public void delete(Post post) {
    postRepository.delete(post);
  }

  public Post findById(long id) {
    return postRepository.findById(id).orElse(null);
  }
}
