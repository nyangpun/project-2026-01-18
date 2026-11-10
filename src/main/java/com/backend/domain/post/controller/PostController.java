package com.backend.domain.post.controller;

import com.backend.domain.board.service.BoardService;
import com.backend.domain.post.dto.PostDto;
import com.backend.domain.post.entity.Post;
import com.backend.domain.post.form.PostForm;
import com.backend.domain.post.service.PostService;
import com.backend.global.security.dto.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/board/{boardId}/post")
@RequiredArgsConstructor
public class PostController {
  private final PostService postService;
  private final BoardService boardService;

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/write")
  public String showWrite(@PathVariable Long boardId, Model model) {
    if (boardService.findById(boardId) == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    model.addAttribute("boardId", boardId);
    model.addAttribute("postForm", new PostForm());
    return "post/write";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/write")
  public String write(@PathVariable Long boardId, PostForm postForm, @AuthenticationPrincipal MemberContext memberContext) {
    if (boardService.findById(boardId) == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    postService.write(postForm.getTitle(), postForm.getContent(), boardId, memberContext.getId());
    return "redirect:/board/" + boardId;
  }

  @GetMapping("/{postId}")
  public String detail(@PathVariable Long boardId, @PathVariable long postId, @AuthenticationPrincipal MemberContext memberContext, Model model) {
    Post post = postService.findById(postId);
    if (post == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    if (!boardId.equals(post.getBoardId())) {
      return "redirect:/board/" + post.getBoardId() + "/post/" + postId;
    }

    model.addAttribute("post", PostDto.from(post));
    model.addAttribute("isAuthor", memberContext != null && memberContext.getId().equals(post.getUserId()));
    return "post/detail";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/{postId}/modify")
  public String showModify(@PathVariable Long boardId, @PathVariable long postId, @AuthenticationPrincipal MemberContext memberContext, Model model) {
    Post post = postService.findById(postId);
    if (post == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    if (!boardId.equals(post.getBoardId())) {
      return "redirect:/board/" + post.getBoardId() + "/post/" + postId + "/modify";
    }
    if (!memberContext.getId().equals(post.getUserId())) {
      throw new AccessDeniedException("작성자만 수정할 수 있습니다.");
    }

    PostForm postForm = new PostForm();
    postForm.setTitle(post.getTitle());
    postForm.setContent(post.getContent());

    model.addAttribute("boardId", boardId);
    model.addAttribute("postId", postId);
    model.addAttribute("postForm", postForm);
    return "post/modify";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/{postId}/modify")
  public String modify(@PathVariable Long boardId, @PathVariable long postId, PostForm postForm, @AuthenticationPrincipal MemberContext memberContext) {
    Post post = postService.findById(postId);
    if (post == null || !boardId.equals(post.getBoardId())) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    if (!memberContext.getId().equals(post.getUserId())) {
      throw new AccessDeniedException("작성자만 수정할 수 있습니다.");
    }

    postService.modify(post, postForm.getTitle(), postForm.getContent());
    return "redirect:/board/" + boardId + "/post/" + postId;
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/{postId}/delete")
  public String delete(@PathVariable Long boardId, @PathVariable long postId, @AuthenticationPrincipal MemberContext memberContext) {
    Post post = postService.findById(postId);
    if (post == null || !boardId.equals(post.getBoardId())) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    if (!memberContext.getId().equals(post.getUserId())) {
      throw new AccessDeniedException("작성자만 삭제할 수 있습니다.");
    }

    postService.delete(post);
    return "redirect:/board/" + boardId;
  }
}
