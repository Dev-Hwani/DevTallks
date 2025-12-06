package com.mysite.devtallks.member.repository;

import com.mysite.devtallks.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Member Repository
 * Spring Data JPA 기반 회원 데이터 접근 레이어
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 이메일로 회원 조회
     */
    Optional<Member> findByEmail(String email);

    /**
     * 사용자명(username)으로 회원 조회
     */
    Optional<Member> findByUsername(String username);

    /**
     * 이메일 중복 체크
     */
    boolean existsByEmail(String email);

    /**
     * 사용자명(username) 중복 체크
     */
    boolean existsByUsername(String username);
}
