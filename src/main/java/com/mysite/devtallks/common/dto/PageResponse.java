package com.mysite.devtallks.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 페이징 처리 공통 DTO
 * @param <T> : 리스트 아이템 타입
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {

    private List<T> content;      // 실제 데이터 리스트
    private int page;             // 현재 페이지
    private int size;             // 페이지 크기
    private long totalElements;   // 총 데이터 수
    private int totalPages;       // 총 페이지 수
    private boolean last;         // 마지막 페이지 여부

    /**
     * 페이징 응답 생성
     */
    public static <T> PageResponse<T> of(List<T> content, int page, int size, long totalElements, int totalPages, boolean last) {
        return PageResponse.<T>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .last(last)
                .build();
    }
}
