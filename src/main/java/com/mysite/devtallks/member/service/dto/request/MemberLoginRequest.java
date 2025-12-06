package com.mysite.devtallks.member.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 요청 DTO
 * - usernameOrEmail 필드 대신 email로 통일 (MemberDetailsService와 연동)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLoginRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
