package com.mysite.devtallks.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 회원 가입/수정 시 이메일이 중복될 때 던지는 예외
 * HTTP 409 (Conflict)로 매핑
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEmailException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String errorCode;

    public DuplicateEmailException() {
        super("이미 사용 중인 이메일입니다.");
        this.errorCode = "DUPLICATE_EMAIL";
    }

    public DuplicateEmailException(String message) {
        super(message);
        this.errorCode = "DUPLICATE_EMAIL";
    }

    public DuplicateEmailException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "DUPLICATE_EMAIL";
    }

    public DuplicateEmailException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public DuplicateEmailException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
