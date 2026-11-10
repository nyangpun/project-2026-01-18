package com.backend.domain.member.controller;

import com.backend.domain.member.dto.MemberProfileDto;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.form.MemberJoinForm;
import com.backend.domain.member.service.MemberService;
import com.backend.global.security.dto.MemberContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String join(MemberJoinForm memberJoinForm, HttpServletRequest req, RedirectAttributes redirectAttributes) {
        String email = memberJoinForm.getEmail();
        String password = memberJoinForm.getPassword();

        Member oldMember = memberService.getMemberByEmail(email);

        if (oldMember != null) {
            return "redirect:/member/joinOldmember";
        }

        try {
            memberService.join(memberJoinForm);
        } catch (MailException e) {
            redirectAttributes.addAttribute("errorMsg", "인증 메일 발송에 실패했습니다. 잠시 후 다시 시도해주세요.");
            return "redirect:/member/join";
        }

        return "redirect:/member/joinsuccess";
    }

    @GetMapping("/verify")
    public String verify(@RequestParam(required = false) String token, Model model) {
        if (memberService.verifyEmail(token)) {
            model.addAttribute("message", "이메일 인증이 완료되었습니다. 로그인해주세요.");
        } else {
            model.addAttribute("message", "인증 링크가 올바르지 않거나 만료되었습니다. 이메일을 입력하면 인증 메일을 다시 보내드립니다.");
            model.addAttribute("showResend", true);
        }
        return "member/verify";
    }

    @GetMapping("/verify/resend")
    public String showResend(Model model) {
        model.addAttribute("message", "가입한 이메일을 입력하면 인증 메일을 다시 보내드립니다.");
        model.addAttribute("showResend", true);
        return "member/verify";
    }

    @PostMapping("/verify/resend")
    public String resend(@RequestParam String email, Model model) {
        try {
            memberService.resendVerificationMail(email);
        } catch (MailException e) {
            // 가입 여부가 드러나지 않도록 화면은 같게 보여준다
        }
        model.addAttribute("message", "해당 주소로 메일을 보냈습니다. 메일함을 확인해주세요.");
        return "member/verify";
    }

    @GetMapping("/joinOldmember")
    public String joinOldmember() { return "member/joinOldmember"; }

    @GetMapping("/joinsuccess")
    public String joinsuccess() { return "member/joinsuccess"; }

    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/loginsuccess")
    public String loginsuccess() { return "member/loginsuccess"; }

    @GetMapping("/loginfail")
    public String loginfail(HttpSession session, Model model) {
        Object exception = session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (exception instanceof DisabledException) {
            model.addAttribute("errorMsg", "이메일 인증이 필요합니다. 메일의 링크를 눌러 인증을 완료해주세요.");
        } else {
            model.addAttribute("errorMsg", "이메일과 비밀번호를 확인해 주세요.");
        }
        return "member/loginfail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        Member member = memberService.getMemberByEmail(memberContext.getEmail());
        model.addAttribute("member", MemberProfileDto.from(member));
        return "member/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public String showModify() {
        return "member/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify")
    public String Modify(@AuthenticationPrincipal MemberContext memberContext, @RequestParam("username") String  username) {
        Member member = memberService.getMemberByEmail( memberContext.getEmail());

        member.setUsername(username);

        memberService.modify(member);

        return "redirect:/member/profile";
    }
}