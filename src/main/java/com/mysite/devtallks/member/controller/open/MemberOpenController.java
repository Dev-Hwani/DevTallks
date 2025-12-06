package com.mysite.devtallks.member.controller.open;

import com.mysite.devtallks.common.dto.ApiResponse;
import com.mysite.devtallks.member.service.MemberService;
import com.mysite.devtallks.member.service.dto.request.MemberLoginRequest;
import com.mysite.devtallks.member.service.dto.request.MemberSignupRequest;
import com.mysite.devtallks.member.service.dto.response.LoginResponse;
import com.mysite.devtallks.member.service.dto.response.MemberResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 공개 API: 회원가입 / 로그인
 * 경로: /auth/*
 */
@RestController
@RequestMapping(path = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MemberOpenController {

    private final MemberService memberService;

    /**
     * 회원가입
     * POST /auth/signup
     */
    @PostMapping("/signup")
    public ApiResponse<MemberResponse> signup(@Valid @RequestBody MemberSignupRequest req) {
        MemberResponse created = memberService.signup(req);
        return ApiResponse.success(created, "회원가입 성공");
    }

    /**
     * 로그인
     * POST /auth/login
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody MemberLoginRequest req) {
        LoginResponse tokens = memberService.login(req);
        return ApiResponse.success(tokens, "로그인 성공");
    }
}
