package com.mysite.devtallks.member.controller;

import com.mysite.devtallks.common.dto.ResponseDto;
import com.mysite.devtallks.member.dto.AuthResponseDTO;
import com.mysite.devtallks.member.dto.MemberRequestDTO;
import com.mysite.devtallks.member.dto.TokenRefreshRequest;
import com.mysite.devtallks.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseDto<AuthResponseDTO> signUp(@Valid @RequestBody MemberRequestDTO.SignUp request) {
        return ResponseDto.ok(memberService.signUp(request));
    }

    @PostMapping("/login")
    public ResponseDto<AuthResponseDTO> login(@Valid @RequestBody MemberRequestDTO.Login request) {
        return ResponseDto.ok(memberService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseDto<AuthResponseDTO> refresh(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseDto.ok(memberService.refreshTokens(request.getRefreshToken()));
    }

    @DeleteMapping("/logout")
    public ResponseDto<Void> logout(@RequestHeader(name = "Authorization", required = false) String authorization,
                                    @Valid @RequestBody TokenRefreshRequest request) {
        String accessToken = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            accessToken = authorization.substring("Bearer ".length());
        }
        memberService.logout(accessToken, request.getRefreshToken());
        return ResponseDto.ok(null, "Logged out");
    }
}
