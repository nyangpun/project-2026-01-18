package com.backend.domain.memeber.repository;

import com.backend.domain.memeber.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // SELECT * FROM member WHERE email = ?
    Optional<Member> findByEmail(String email);
}