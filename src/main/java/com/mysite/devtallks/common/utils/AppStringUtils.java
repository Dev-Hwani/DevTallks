package com.mysite.devtallks.common.utils;

import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * 문자열 유틸 (Spring의 StringUtils와 이름 충돌 안 나게 AppStringUtils로 분리)
 */
public final class AppStringUtils {

    private AppStringUtils() {}

    public static boolean isBlank(@Nullable String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean hasText(@Nullable String s) {
        return !isBlank(s);
    }

    public static String defaultIfBlank(@Nullable String s, String defaultValue) {
        return isBlank(s) ? defaultValue : s;
    }

    public static String truncate(String s, int maxLength) {
        if (s == null) return null;
        if (maxLength <= 0) return "";
        if (s.length() <= maxLength) return s;
        return s.substring(0, maxLength);
    }

    public static String safeTrim(@Nullable String s) {
        return s == null ? null : s.trim();
    }
}
