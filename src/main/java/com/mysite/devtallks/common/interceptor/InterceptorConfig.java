package com.mysite.devtallks.common.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;
    private final RequestValidationInterceptor requestValidationInterceptor;
    private final ExecutionTimeInterceptor executionTimeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 순서 매우 중요!
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**");

        registry.addInterceptor(requestValidationInterceptor)
                .addPathPatterns("/api/**");

        registry.addInterceptor(executionTimeInterceptor)
                .addPathPatterns("/**");
    }
}
