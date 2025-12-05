package com.mysite.devtallks.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.devtallks.common.dto.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 요청 시 JWT가 있으면 검증하고 SecurityContext에 Authentication 설정
 *
 * - Authorization 헤더에서 Bearer 토큰 추출
 * - 토큰 유효하면 UserDetailsService로 사용자 로드 후 Authentication 설정
 *
 * 주의: member 도메인에 UserDetailsService 구현체가 있어야 함
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = resolveToken(request);
            if (StringUtils.hasText(token)) {
                boolean valid = jwtTokenProvider.validateToken(token);
                if (valid) {
                    String subject = jwtTokenProvider.getSubject(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    // 토큰이 유효하지 않으면 인증 실패로 처리(EntryPoint가 처리)
                    log.debug("JWT token is invalid for request: {}", request.getRequestURI());
                }
            }
        } catch (Exception ex) {
            log.warn("Could not set user authentication: {}", ex.getMessage());
            // 여기서는 바로 응답하지 않고 필터 체인을 계속 진행시켜 AuthenticationEntryPoint에서 처리하도록 함
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Authorization 헤더에서 "Bearer {token}" 추출
     */
    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader(JwtConstants.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearer) && bearer.startsWith(JwtConstants.BEARER_PREFIX)) {
            return bearer.substring(JwtConstants.BEARER_PREFIX.length());
        }
        return null;
    }
}
