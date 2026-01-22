package com.backend.domain.memeber.from;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinForm {
    String username;
    String password;
    String email;
}