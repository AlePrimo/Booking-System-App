package com.aleprimo.Booking_System_App.service;


import com.aleprimo.Booking_System_App.entity.Booking;
import com.aleprimo.Booking_System_App.entity.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BookingService {
    Booking createBooking(Booking booking);
    Booking updateBooking(Long id, Booking booking);
    Booking updateBookingStatus(Long id, BookingStatus status);
    void deleteBooking(Long id);
    Optional<Booking> getBookingById(Long id);
    Page<Booking> getAllBookings(Pageable pageable);
    Page<Booking> getBookingsByCustomerId(Long customerId, Pageable pageable);
    Page<Booking> getBookingsByOfferingId(Long offeringId, Pageable pageable);
    Page<Booking> getBookingsByStatus(BookingStatus status, Pageable pageable);
    Page<Booking> getBookingsBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
