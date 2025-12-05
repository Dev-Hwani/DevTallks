package com.mysite.devtallks.common.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

/**
 * 날짜/시간 유틸
 * - ISO 포맷, KST 포맷 등의 편의 메서드를 제공
 */
public final class DateTimeUtil {

    private DateTimeUtil() {}

    public static final ZoneId KST = ZoneId.of("Asia/Seoul");
    public static final DateTimeFormatter ISO = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    public static final DateTimeFormatter DATE_TIME_SIMPLE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String nowIso() {
        return OffsetDateTime.now().format(ISO);
    }

    public static String nowKst() {
        return LocalDateTime.now(KST).format(DATE_TIME_SIMPLE);
    }

    public static String format(LocalDateTime dt) {
        if (dt == null) return null;
        return dt.format(DATE_TIME_SIMPLE);
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) return null;
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date toDate(LocalDateTime ldt) {
        if (ldt == null) return null;
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
}
