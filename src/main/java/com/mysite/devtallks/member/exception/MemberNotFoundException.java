package com.mysite.devtallks.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 회원을 찾을 수 없을 때 던지는 예외
 * HTTP 404 (Not Found)로 매핑
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MemberNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String errorCode;

    public MemberNotFoundException() {
        super("회원 정보를 찾을 수 없습니다.");
        this.errorCode = "MEMBER_NOT_FOUND";
    }

    public MemberNotFoundException(String message) {
        super(message);
        this.errorCode = "MEMBER_NOT_FOUND";
    }

    public MemberNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "MEMBER_NOT_FOUND";
    }

    public MemberNotFoundException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public MemberNotFoundException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
