package com.aleprimo.Booking_System_App.util;


import com.aleprimo.Booking_System_App.dto.PageResponse;
import org.springframework.data.domain.Page;

public class PageResponseUtil {

    public static <T> PageResponse<T> from(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
