package com.aleprimo.Booking_System_App.config;

import com.aleprimo.Booking_System_App.BookingSystemAppApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

class BookingSystemAppApplicationTest {

    @Test
    void main_shouldRunWithoutErrors() {
        String[] args = {};
        BookingSystemAppApplication.main(args);
        // No assertion needed, just runs main to cover the line
    }
}
