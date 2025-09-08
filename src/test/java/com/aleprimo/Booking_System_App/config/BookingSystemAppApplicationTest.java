package com.aleprimo.Booking_System_App.config;

import com.aleprimo.Booking_System_App.BookingSystemAppApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@ActiveProfiles("test")
@SpringBootTest
class BookingSystemAppApplicationTest {



    @Test
    void constructor_coverage() throws Exception {
        var constructor = BookingSystemAppApplication.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    void main_shouldRunWithoutCrashing() {
        String[] args = {};
        assertDoesNotThrow(() -> BookingSystemAppApplication.main(args));


    }




}
