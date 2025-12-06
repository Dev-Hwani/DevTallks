package com.mysite.devtallks.member.service;

import com.mysite.devtallks.member.service.dto.request.MemberLoginRequest;
import com.mysite.devtallks.member.service.dto.request.MemberSignupRequest;
import com.mysite.devtallks.member.service.dto.request.MemberUpdateRequest;
import com.mysite.devtallks.member.service.dto.response.LoginResponse;
import com.mysite.devtallks.member.service.dto.response.MemberResponse;

public interface MemberService {

    /**
     * 회원가입
     */
    MemberResponse signup(MemberSignupRequest request);

    /**
     * 로그인 -> Access + Refresh 토큰 반환
     */
    LoginResponse login(MemberLoginRequest request);

    /**
     * 회원 단건 조회
     */
    MemberResponse findById(Long memberId);

    /**
     * 현재 로그인한 사용자 정보 조회 (username/email 기준)
     */
    MemberResponse findByUsername(String username);

    /**
     * 회원 정보 수정 (닉네임 등)
     */
    MemberResponse updateMember(Long memberId, MemberUpdateRequest request);

    /**
     * 회원 탈퇴 (soft 삭제 또는 실제 삭제)
     */
    void deleteMember(Long memberId);
}
