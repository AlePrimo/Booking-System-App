package com.aleprimo.Booking_System_App.service;


import com.aleprimo.Booking_System_App.entity.Booking;
import com.aleprimo.Booking_System_App.entity.Offering;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.BookingStatus;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import com.aleprimo.Booking_System_App.persistence.BookingDAO;
import com.aleprimo.Booking_System_App.persistence.UserDAO;
import com.aleprimo.Booking_System_App.service.serviceImpl.BookingServiceImpl;
import com.aleprimo.Booking_System_App.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingDAO bookingDAO;
    @Mock
    private UserDAO userDAO;
    @InjectMocks
    private BookingServiceImpl bookingService;
    @InjectMocks
    private UserServiceImpl userService;

    private Booking booking;
    private User customer;
    private User provider;

    @BeforeEach
    void setUp() {
        customer = User.builder()
                .id(1L)
                .name("Juan")
                .email("juan@mail.com")
                .password("1234")
                .roles(Set.of(Role.CUSTOMER))
                .build();
        userService.createUser(customer);

        provider = User.builder()
                .name("VANDALAY")
                .email("vandalay@mail")
                .password("vandalay124")
                .roles(Set.of(Role.PROVIDER))
                .build();
        userService.createUser(provider);

        Offering offering = Offering.builder()
                .id(1L)
                .name("Service Test")
                .price(BigDecimal.valueOf(200.0))
                .provider(provider)
                .durationMinutes(15)
                .description("destapamiento del caño")
                .build();

        booking = Booking.builder()
                .id(1L)
                .bookingDateTime(LocalDateTime.now().plusDays(1))
                .status(BookingStatus.PENDING)
                .customer(customer)
                .offering(offering)
                .build();
    }

    @Test
    void testCreateBooking() {
        when(bookingDAO.save(booking)).thenReturn(booking);

        Booking saved = bookingService.createBooking(booking);

        assertThat(saved.getId()).isEqualTo(1L);
        verify(bookingDAO, times(1)).save(booking);
    }

    @Test
    void testGetBookingById() {
        when(bookingDAO.findById(1L)).thenReturn(Optional.of(booking));

        Optional<Booking> found = bookingService.getBookingById(1L);

        assertThat(found).isPresent();
    }

    @Test
    void testUpdateBookingStatus() {
        when(bookingDAO.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingDAO.save(any(Booking.class))).thenReturn(booking);

        Booking updated = bookingService.updateBookingStatus(1L, BookingStatus.CONFIRMED);

        assertThat(updated.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
    }
}
