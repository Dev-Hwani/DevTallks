package com.mysite.devtallks.member.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 정보 수정 DTO
 * - 닉네임 변경, 비밀번호 변경 등
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateRequest {
    private String nickname;
    private String newPassword;
}
