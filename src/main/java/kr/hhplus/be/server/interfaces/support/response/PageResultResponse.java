package kr.hhplus.be.server.interfaces.support.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResultResponse<T>(
        Boolean result,
        Integer totalPages,
        Long totalCount,
        Integer currentCount,
        T data
) {
    public static <T> PageResultResponse<List<T>> of(Page<T> page) {
        return new PageResultResponse<>(
                true,
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumberOfElements(),
                page.getContent()
        );
    }

    public static <T> PageResultResponse<T> error() {
        return new PageResultResponse<>(
                false,
                0,
                0L,
                0,
                null
        );
    }
}