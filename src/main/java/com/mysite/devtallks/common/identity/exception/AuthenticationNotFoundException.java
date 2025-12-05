package com.mysite.devtallks.common.identity.exception;

public class AuthenticationNotFoundException extends RuntimeException {
    public AuthenticationNotFoundException() {
        super("현재 요청에 인증 정보가 없습니다.");
    }
}
