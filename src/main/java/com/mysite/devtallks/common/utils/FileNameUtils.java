package com.mysite.devtallks.common.utils;

import java.text.Normalizer;
import java.util.UUID;

/**
 * 파일명 관련 유틸
 * - 업로드 시 안전한 파일명 생성, 확장자 추출 등
 */
public final class FileNameUtils {

    private FileNameUtils() {}

    /**
     * 원본 파일명에서 확장자(소문자)를 반환. 확장자가 없으면 빈 문자열 반환.
     */
    public static String extractExtension(String original) {
        if (original == null) return "";
        int idx = original.lastIndexOf('.');
        if (idx == -1 || idx == original.length() - 1) return "";
        return original.substring(idx + 1).toLowerCase();
    }

    /**
     * 안전한 파일명 생성 (UUID + 확장자)
     */
    public static String generateSafeFileName(String original) {
        String ext = extractExtension(original);
        String base = UUID.randomUUID().toString().replace("-", "");
        if (ext.isEmpty()) return base;
        return base + "." + ext;
    }

    /**
     * 파일명에서 위험 문자 제거 (normalize + basic replace)
     */
    public static String sanitizeFileName(String filename) {
        if (filename == null) return null;
        String n = Normalizer.normalize(filename, Normalizer.Form.NFKD);
        // Remove path separators and control chars
        n = n.replaceAll("[\\\\/\\r\\n\\t\\0]", "_");
        // Remove other unsafe chars
        n = n.replaceAll("[<>:\"|?*]+", "_");
        return n;
    }
}
