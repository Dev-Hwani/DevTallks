package com.mysite.devtallks.common.dto;

import java.time.LocalDateTime;

public class ResponseDto<T> {

    private final boolean success;
    private final T data;
    private final String message;
    private final LocalDateTime timestamp;

    private ResponseDto(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ResponseDto<T> ok(T data) {
        return new ResponseDto<>(true, data, null);
    }

    public static <T> ResponseDto<T> ok(T data, String message) {
        return new ResponseDto<>(true, data, message);
    }

    public static <T> ResponseDto<T> fail(String message) {
        return new ResponseDto<>(false, null, message);
    }

    public static <T> ResponseDto<T> fail(String message, T data) {
        return new ResponseDto<>(false, data, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
