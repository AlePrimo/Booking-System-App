package com.aleprimo.Booking_System_App.config;

import com.aleprimo.Booking_System_App.dto.PageResponse;
import com.aleprimo.Booking_System_App.util.PageResponseUtil;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageResponseUtilTest {

    @Test
    void testFrom() {
        Page<String> page = new PageImpl<>(List.of("A", "B"), PageRequest.of(0, 2), 2);
        PageResponse<String> response = PageResponseUtil.from(page);

        assertThat(response.getContent()).containsExactly("A", "B");
        assertThat(response.getPageNumber()).isEqualTo(0);
        assertThat(response.getPageSize()).isEqualTo(2);
        assertThat(response.getTotalElements()).isEqualTo(2);
        assertThat(response.getTotalPages()).isEqualTo(1);
        assertThat(response.isLast()).isTrue();
    }

    @Test
    void testPrivateConstructor() throws Exception {
        // Llamada al constructor implícito de la clase utilitaria para cobertura Jacoco
        var constructor = PageResponseUtil.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
