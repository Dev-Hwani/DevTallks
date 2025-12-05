package com.mysite.devtallks.common.redis;

import org.springframework.util.StringUtils;

/**
 * Redis Key 네이밍 규칙 유틸
 * 예: RedisKeyUtil.of("rate", ip, method, path) -> "rate:1.2.3.4:GET:/api/x"
 */
public final class RedisKeyUtil {

    private RedisKeyUtil() {}

    public static String of(String prefix, String... parts) {
        StringBuilder sb = new StringBuilder(prefix);
        for (String p : parts) {
            if (p == null) continue;
            String clean = p.trim();
            if (!StringUtils.hasText(clean)) continue;
            sb.append(':').append(clean.replaceAll("\\s+", "_"));
        }
        return sb.toString();
    }
}
