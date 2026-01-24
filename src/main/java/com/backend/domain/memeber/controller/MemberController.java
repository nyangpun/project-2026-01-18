package com.backend.domain.memeber.controller;

import com.backend.domain.memeber.entity.Member;
import com.backend.domain.memeber.from.MemberJoinForm;
import com.backend.domain.memeber.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/join")
    public String showJoin() {
        return "member/join";
    }

    @PostMapping("/join")
    public String join(MemberJoinForm memberJoinForm, HttpServletRequest req) {
        String email = memberJoinForm.getEmail();
        String password = memberJoinForm.getPassword();

        Member oldMember = memberService.getMemberByEmail(email);

        if (oldMember != null) {
            return "redirect:/?errorMsg=Already exists email";
        }

        Member member = memberService.join(memberJoinForm);

        try {
            req.login(email, password); // 로그인 처리
            System.out.println("로그인 성공");
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "member/login";
    }
}