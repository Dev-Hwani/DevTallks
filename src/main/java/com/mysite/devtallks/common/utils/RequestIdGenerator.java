package com.mysite.devtallks.common.utils;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

/**
 * Request ID 생성 유틸
 * - 간단하고 고유한 request id를 생성하는 메소드 제공
 * - Interceptor / MDC / 로그에 사용
 */
public final class RequestIdGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private RequestIdGenerator() {}

    /**
     * UUID 기반 request id
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 짧은 랜덤 Base64 URL-safe ID (예: 16바이트 -> ~22문자)
     */
    public static String generateShortId(int byteLength) {
        byte[] b = new byte[Math.max(8, byteLength)];
        RANDOM.nextBytes(b);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
    }

    public static String generateShortId() {
        return generateShortId(12);
    }
}
