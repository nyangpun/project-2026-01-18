package com.backend.domain.member.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinForm {
    String username;
    String password;
    String email;
}