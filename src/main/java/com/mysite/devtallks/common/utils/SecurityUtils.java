package com.mysite.devtallks.common.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * SecurityContext 관련 헬퍼
 * - JwtAuthenticationFilter 와 identity 패키지와 호환
 */
public final class SecurityUtils {

    private SecurityUtils() {}

    /**
     * 현재 인증된 Authentication 객체 획득 (없으면 null)
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 현재 인증된 username (principal이 UserDetails면 getUsername(), 아니면 principal.toString())
     */
    public static String getCurrentUsername() {
        Authentication auth = getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;
        Object p = auth.getPrincipal();
        if (p instanceof UserDetails) {
            return ((UserDetails) p).getUsername();
        }
        return p == null ? null : p.toString();
    }

    public static boolean isAuthenticated() {
        Authentication auth = getAuthentication();
        return auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
    }
}
