package com.backend.domain.member.service;

import com.backend.domain.member.entity.EmailVerification;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.form.MemberJoinForm;
import com.backend.domain.member.repository.EmailVerificationRepository;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.global.app.AppConfig;
import com.backend.global.security.dto.MemberContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationRepository emailVerificationRepository;
    private final JavaMailSender mailSender;

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public Member join(MemberJoinForm memberJoinForm) {

        String passwordClearText = memberJoinForm.getPassword();
        String password = passwordEncoder.encode(passwordClearText);

        Member member = Member.builder()
                .username(memberJoinForm.getEmail())
                .email(memberJoinForm.getEmail())
                .password(password)
                .build();

        memberRepository.save(member);

        sendVerificationMail(member);

        return member;
    }

    @Transactional
    public boolean verifyEmail(String token) {
        if (token == null) {
            return false;
        }

        EmailVerification emailVerification = emailVerificationRepository.findByToken(token).orElse(null);
        if (emailVerification == null || emailVerification.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        Member member = memberRepository.findById(emailVerification.getMemberId()).orElse(null);
        if (member == null) {
            return false;
        }

        member.setEmailVerified(true);
        emailVerificationRepository.delete(emailVerification);
        return true;
    }

    @Transactional
    public void resendVerificationMail(String email) {
        Member member = getMemberByEmail(email);
        if (member == null || member.isEmailVerified()) {
            return;
        }

        emailVerificationRepository.deleteByMemberId(member.getId());
        sendVerificationMail(member);
    }

    private void sendVerificationMail(Member member) {
        String token = UUID.randomUUID().toString();

        EmailVerification emailVerification = EmailVerification.builder()
                .token(token)
                .memberId(member.getId())
                .expiresAt(LocalDateTime.now().plusHours(24))
                .build();
        emailVerificationRepository.save(emailVerification);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(member.getEmail());
        message.setSubject("[회원가입] 이메일 인증을 완료해주세요");
        message.setText("아래 링크를 눌러 이메일 인증을 완료해주세요. (24시간 동안 유효합니다)\n\n"
                + AppConfig.getSiteBackUrl() + "/member/verify?token=" + token);
        mailSender.send(message);
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 사용자 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("email not found: " + email));


        // 권한 설정
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + member.getRole()));

        return new MemberContext(member, authorities);
    }

    public Member join(String username, String password, String email) {
        Member member = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .emailVerified(true)
                .build();

        memberRepository.save(member);

        return member;
    }

    // SELECT COUNT(*) FROM `member`;
    public long count() {
        return memberRepository.count();
    }

    public void modify(Member member) {
        memberRepository.save(member);
    }
}
