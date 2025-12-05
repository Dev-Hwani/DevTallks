package com.mysite.devtallks.common.utils;

import com.mysite.devtallks.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;

/**
 * ApiResponse 도우미
 * - common.dto.ApiResponse 구조를 편하게 생성하기 위한 헬퍼
 */
public final class ResponseUtils {

    private ResponseUtils() {}

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> fail(int httpStatus, String message) {
        return ResponseEntity.status(httpStatus).body(ApiResponse.fail(message));
    }

    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message) {
        return fail(400, message);
    }

    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(String message) {
        return fail(401, message);
    }
}
