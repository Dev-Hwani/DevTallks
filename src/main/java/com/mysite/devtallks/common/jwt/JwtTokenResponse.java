package com.mysite.devtallks.common.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 토큰 발급 결과 DTO
 */
@Getter
@AllArgsConstructor
public class JwtTokenResponse {
    private final String accessToken;
    private final String refreshToken;
    private final long accessTokenExpiresIn;
    private final long refreshTokenExpiresIn;
}
