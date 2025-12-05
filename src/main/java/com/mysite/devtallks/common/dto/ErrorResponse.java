package com.mysite.devtallks.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 예외 발생 시 반환 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private LocalDateTime timestamp;   // 에러 발생 시간
    private int status;                // HTTP 상태 코드
    private String error;              // 에러 타입
    private String message;            // 에러 메시지
    private String path;               // 요청 URL
}
