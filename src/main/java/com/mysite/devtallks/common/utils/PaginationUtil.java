package com.mysite.devtallks.common.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 페이징 유틸
 * - 컨트롤러에서 요청 파라미터를 받아 Pageable 생성할 때 사용
 */
public final class PaginationUtil {

    private PaginationUtil() {}

    /**
     * page: 0-based
     * size: 페이지 사이즈
     * sortField: 정렬 필드 (null이면 id)
     * direction: "asc" or "desc"
     */
    public static Pageable of(Integer page, Integer size, String sortField, String direction) {
        int p = page == null || page < 0 ? 0 : page;
        int s = size == null || size <= 0 ? 20 : size;
        String field = AppStringUtils.hasText(sortField) ? sortField : "id";
        Sort.Direction dir = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(p, s, Sort.by(dir, field));
    }
}
