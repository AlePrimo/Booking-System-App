package com.aleprimo.Booking_System_App.config;

import com.aleprimo.Booking_System_App.BookingSystemAppApplication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


class BookingSystemAppApplicationTest {


    @Test
    void constructor_coverage() throws Exception {
        var constructor = BookingSystemAppApplication.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    void main_shouldRunWithoutCrashing() {
        String[] args = {
                "--spring.main.web-application-type=none",
                "--spring.main.lazy-initialization=true", 
                "--spring.autoconfigure.exclude=" +
                        "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
                        "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration," +
                        "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration," +
                        "org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration" // 👈 evita BookingRepository
        };

        assertDoesNotThrow(() -> BookingSystemAppApplication.main(args));
    }





}
