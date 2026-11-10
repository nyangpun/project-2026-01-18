package com.backend.domain.board.controller;

import com.backend.domain.board.dto.BoardDto;
import com.backend.domain.board.entity.Board;
import com.backend.domain.board.form.BoardForm;
import com.backend.domain.board.service.BoardService;
import com.backend.domain.post.dto.PostDto;
import com.backend.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final PostService postService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("boards", boardService.findAll().stream().map(BoardDto::from).toList());
        return "board/list";
    }

    @GetMapping("/{boardId}")
    public String detail(@PathVariable long boardId, Model model) {
        Board board = boardService.findById(boardId);
        if (board == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("board", BoardDto.from(board));
        model.addAttribute("posts", postService.findByBoardId(boardId).stream().map(PostDto::from).toList());
        return "board/detail";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String showNew(Model model) {
        model.addAttribute("boardForm", new BoardForm());
        return "board/new";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public String create(BoardForm boardForm) {
        Board board = boardService.create(boardForm.getGameId(), boardForm.getTitle());
        return "redirect:/board/" + board.getId();
    }
}
