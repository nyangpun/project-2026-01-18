package com.backend.global.initData;

import com.backend.domain.memeber.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
public class TestInitData {

    @Bean
    CommandLineRunner commandLineRunner(MemberService memberService, PasswordEncoder passwordEncoder) {
        return args -> {
            if (memberService.count() > 0)
                return;
            String password = passwordEncoder.encode("1234");
            memberService.join("user1@test.com", password,"user1@test.com");
            memberService.join("user2@test.com", password,"user2@test.com");
        };
    }
}
