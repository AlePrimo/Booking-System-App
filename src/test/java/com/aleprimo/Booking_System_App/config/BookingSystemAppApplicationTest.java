package com.aleprimo.Booking_System_App.config;

import com.aleprimo.Booking_System_App.BookingSystemAppApplication;
import org.junit.jupiter.api.Test;


import java.lang.reflect.Constructor;

class BookingSystemAppApplicationTest {


    @Test
    void constructor_coverage() throws Exception {
        Constructor<BookingSystemAppApplication> constructor =
                BookingSystemAppApplication.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
