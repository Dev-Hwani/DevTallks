package com.mysite.devtallks.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 회원 가입/수정 시 username(사용자명)이 중복될 때 던지는 예외
 * HTTP 409 (Conflict)로 매핑
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateUsernameException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String errorCode;

    public DuplicateUsernameException() {
        super("이미 사용 중인 사용자명입니다.");
        this.errorCode = "DUPLICATE_USERNAME";
    }

    public DuplicateUsernameException(String message) {
        super(message);
        this.errorCode = "DUPLICATE_USERNAME";
    }

    public DuplicateUsernameException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "DUPLICATE_USERNAME";
    }

    public DuplicateUsernameException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public DuplicateUsernameException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
