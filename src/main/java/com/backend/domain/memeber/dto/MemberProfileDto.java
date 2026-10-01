package com.backend.domain.memeber.dto;

import com.backend.domain.memeber.entity.Member;

public record MemberProfileDto(String username, String email) {
    public static MemberProfileDto from(Member member) {
        return new MemberProfileDto(member.getUsername(), member.getEmail());
    }
}
