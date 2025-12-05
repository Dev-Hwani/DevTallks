package com.mysite.devtallks.common.utils;

import java.util.regex.Pattern;

/**
 * 간단한 검증 유틸
 */
public final class ValidationUtils {

    private ValidationUtils() {}

    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$");
    private static final Pattern FILENAME = Pattern.compile("^[^\\\\/<>:\"|?*]+$");

    /**
     * 이메일 포맷 체크
     */
    public static boolean isEmail(String s) {
        if (s == null) return false;
        return EMAIL.matcher(s.trim()).matches();
    }

    /**
     * 파일명 유효성 체크(심플)
     */
    public static boolean isSafeFileName(String filename) {
        if (filename == null) return false;
        return FILENAME.matcher(filename).matches();
    }

    /**
     * 안전한 패스워드 체크(간단한 예시, 필요시 정책 강화)
     * - 최소 길이 8, 숫자 포함, 문자 포함
     */
    public static boolean isStrongPassword(String password) {
        if (password == null) return false;
        String p = password.trim();
        if (p.length() < 8) return false;
        boolean hasDigit = p.chars().anyMatch(Character::isDigit);
        boolean hasLetter = p.chars().anyMatch(Character::isLetter);
        return hasDigit && hasLetter;
    }
}
