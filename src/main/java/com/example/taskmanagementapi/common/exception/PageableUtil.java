package com.example.taskmanagementapi.common.exception;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageableUtil {
    private PageableUtil() {}

    public static Pageable withDefaultSort(
            Pageable pageable,
            String defaultSortField
    ) {
        if (pageable.getSort().isSorted()) {
            return pageable;
        }

        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, defaultSortField)
        );
    }
}
