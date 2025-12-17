package com.mysite.devtallks.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuthResponseDTO {
    private final MemberResponseDTO member;
    private final String accessToken;
    private final String refreshToken;
}
