package com.backend.domain.member.dto;

import com.backend.domain.member.entity.Member;

public record MemberProfileDto(String username, String email) {
    public static MemberProfileDto from(Member member) {
        return new MemberProfileDto(member.getUsername(), member.getEmail());
    }
}
