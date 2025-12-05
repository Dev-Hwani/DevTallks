package com.mysite.devtallks.common.jwt;

/**
 * JWT 관련 상수
 */
public final class JwtConstants {
    private JwtConstants() {}

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
    public static final String TOKEN_TYPE = "Bearer";
}
