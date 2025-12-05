package com.mysite.devtallks.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.lang.Nullable;

/**
 * Jackson ObjectMapper 래퍼 유틸
 * - null/exception 방어형 메서드 제공
 * - 프로젝트 전역에서 ObjectMapper 주입 대신 간단 호출로 사용 가능
 */
public final class JsonUtils {

    private static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper();

    private JsonUtils() {}

    public static String toJson(@Nullable Object obj) {
        if (obj == null) return null;
        try {
            return DEFAULT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON serialization error: " + e.getMessage(), e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null) return null;
        try {
            return DEFAULT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("JSON deserialization error: " + e.getMessage(), e);
        }
    }

    public static ObjectMapper mapper() {
        return DEFAULT_MAPPER;
    }
}
