package com.backend.domain.home.controller;

import com.backend.domain.memeber.entity.Member;
import com.backend.domain.memeber.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class HomeController {
  private final MemberService memberService;

  @GetMapping("/")
  public String home(Principal principal, Model model) {

    Member loginedMember = null;

    if(principal != null && principal.getName() != null) {
      loginedMember = memberService.getMemberByEmail(principal.getName());
    }

    model.addAttribute("loginedMember", loginedMember);
    return "home/index"; // Changed from "home" to "home/index"
  }
}