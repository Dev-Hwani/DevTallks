package com.mysite.devtallks.common.interceptor;

import com.mysite.devtallks.common.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestValidationInterceptor implements org.springframework.web.servlet.HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // 기본 헤더 검증 (예: API 요청은 대부분 JSON)
        if (!"GET".equalsIgnoreCase(request.getMethod())) {
            if (!MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())) {
                ApiResponse<?> body = ApiResponse.fail("지원되지 않는 Content-Type 입니다.");
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(objectMapper.writeValueAsString(body));
                return false;
            }
        }

        return true;
    }
}
