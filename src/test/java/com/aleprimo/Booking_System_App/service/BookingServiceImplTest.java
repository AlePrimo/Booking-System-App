package com.aleprimo.Booking_System_App.service;


import com.aleprimo.Booking_System_App.entity.Booking;
import com.aleprimo.Booking_System_App.entity.Offering;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.BookingStatus;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import com.aleprimo.Booking_System_App.exception.ResourceNotFoundException;
import com.aleprimo.Booking_System_App.persistence.BookingDAO;

import com.aleprimo.Booking_System_App.service.serviceImpl.BookingServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {



    @Mock
    private BookingDAO bookingDAO;

    @InjectMocks
    private BookingServiceImpl bookingService;

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
                .roles(Set.of(Role.ROLE_CUSTOMER))
                .build();

        provider = User.builder()
                .id(2L)
                .name("VANDALAY")
                .email("vandalay@mail")
                .password("vandalay124")
                .roles(Set.of(Role.ROLE_PROVIDER))
                .build();

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

    // ==================================
    // TESTS CRUD
    // ==================================

    @Test
    void testCreateBooking() {
        when(bookingDAO.save(booking)).thenReturn(booking);
        Booking saved = bookingService.createBooking(booking);
        assertThat(saved).isEqualTo(booking);
        verify(bookingDAO, times(1)).save(booking);
        verifyNoMoreInteractions(bookingDAO);
    }

    @Test
    void testGetBookingByIdFound() {
        when(bookingDAO.findById(1L)).thenReturn(Optional.of(booking));
        Optional<Booking> found = bookingService.getBookingById(1L);
        assertThat(found).isPresent().contains(booking);
        verify(bookingDAO, times(1)).findById(1L);
        verifyNoMoreInteractions(bookingDAO);
    }

    @Test
    void testGetBookingByIdNotFound() {
        when(bookingDAO.findById(99L)).thenReturn(Optional.empty());
        Optional<Booking> found = bookingService.getBookingById(99L);
        assertThat(found).isEmpty();
        verify(bookingDAO, times(1)).findById(99L);
        verifyNoMoreInteractions(bookingDAO);
    }

    @Test
    void testUpdateBookingSuccess() {
        Booking updateData = Booking.builder()
                .customer(customer)
                .status(BookingStatus.CONFIRMED)
                .bookingDateTime(LocalDateTime.now().plusDays(2))
                .offering(booking.getOffering())
                .build();

        when(bookingDAO.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingDAO.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

        Booking updated = bookingService.updateBooking(1L, updateData);

        assertThat(updated.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
        assertThat(updated.getBookingDateTime()).isEqualTo(updateData.getBookingDateTime());
        assertThat(updated.getCustomer()).isEqualTo(customer);

        verify(bookingDAO, times(1)).findById(1L);
        verify(bookingDAO, times(1)).save(booking);
        verifyNoMoreInteractions(bookingDAO);
    }

    @Test
    void testUpdateBookingNotFound() {
        when(bookingDAO.findById(99L)).thenReturn(Optional.empty());
        Booking updateData = Booking.builder().build();
        assertThrows(ResourceNotFoundException.class, () -> bookingService.updateBooking(99L, updateData));
        verify(bookingDAO, times(1)).findById(99L);
        verifyNoMoreInteractions(bookingDAO);
    }

    @Test
    void testUpdateBookingStatusSuccess() {
        when(bookingDAO.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingDAO.save(any(Booking.class))).thenReturn(booking);

        Booking updated = bookingService.updateBookingStatus(1L, BookingStatus.CONFIRMED);

        assertThat(updated.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
        verify(bookingDAO, times(1)).findById(1L);
        verify(bookingDAO, times(1)).save(booking);
        verifyNoMoreInteractions(bookingDAO);
    }

    @Test
    void testUpdateBookingStatusNotFound() {
        when(bookingDAO.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookingService.updateBookingStatus(99L, BookingStatus.CONFIRMED));
        verify(bookingDAO, times(1)).findById(99L);
        verifyNoMoreInteractions(bookingDAO);
    }

    @Test
    void testDeleteBooking() {
        doNothing().when(bookingDAO).deleteById(1L);
        bookingService.deleteBooking(1L);
        verify(bookingDAO, times(1)).deleteById(1L);
        verifyNoMoreInteractions(bookingDAO);
    }

    // ==================================
    // TESTS PAGINACION
    // ==================================

    @Test
    void testGetAllBookings() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> page = new PageImpl<>(List.of(booking));
        when(bookingDAO.findAll(pageable)).thenReturn(page);

        Page<Booking> result = bookingService.getAllBookings(pageable);

        assertThat(result.getContent()).containsExactly(booking);
        verify(bookingDAO, times(1)).findAll(pageable);
        verifyNoMoreInteractions(bookingDAO);
    }

    @Test
    void testGetBookingsByCustomerId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> page = new PageImpl<>(List.of(booking));
        when(bookingDAO.findByCustomerId(1L, pageable)).thenReturn(page);

        Page<Booking> result = bookingService.getBookingsByCustomerId(1L, pageable);

        assertThat(result.getContent()).containsExactly(booking);
        verify(bookingDAO, times(1)).findByCustomerId(1L, pageable);
        verifyNoMoreInteractions(bookingDAO);
    }

    @Test
    void testGetBookingsByOfferingId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> page = new PageImpl<>(List.of(booking));
        when(bookingDAO.findByOfferingId(1L, pageable)).thenReturn(page);

        Page<Booking> result = bookingService.getBookingsByOfferingId(1L, pageable);

        assertThat(result.getContent()).containsExactly(booking);
        verify(bookingDAO, times(1)).findByOfferingId(1L, pageable);
        verifyNoMoreInteractions(bookingDAO);
    }

    @Test
    void testGetBookingsByStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> page = new PageImpl<>(List.of(booking));
        when(bookingDAO.findByStatus(BookingStatus.PENDING, pageable)).thenReturn(page);

        Page<Booking> result = bookingService.getBookingsByStatus(BookingStatus.PENDING, pageable);

        assertThat(result.getContent()).containsExactly(booking);
        verify(bookingDAO, times(1)).findByStatus(BookingStatus.PENDING, pageable);
        verifyNoMoreInteractions(bookingDAO);
    }

    @Test
    void testGetBookingsBetween() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        Page<Booking> page = new PageImpl<>(List.of(booking));
        when(bookingDAO.findByBookingDateTimeBetween(start, end, pageable)).thenReturn(page);

        Page<Booking> result = bookingService.getBookingsBetween(start, end, pageable);

        assertThat(result.getContent()).containsExactly(booking);
        verify(bookingDAO, times(1)).findByBookingDateTimeBetween(start, end, pageable);
        verifyNoMoreInteractions(bookingDAO);
    }







}
