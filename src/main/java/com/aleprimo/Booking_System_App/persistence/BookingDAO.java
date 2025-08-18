package com.aleprimo.Booking_System_App.persistence;


import com.aleprimo.Booking_System_App.entity.Booking;
import com.aleprimo.Booking_System_App.entity.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BookingDAO {
    Booking save(Booking booking);
    Optional<Booking> findById(Long id);
    Page<Booking> findAll(Pageable pageable);
    Page<Booking> findByCustomerId(Long customerId, Pageable pageable);
    Page<Booking> findByOfferingId(Long offeringId, Pageable pageable);
    Page<Booking> findByStatus(BookingStatus status, Pageable pageable);
    Page<Booking> findByBookingDateTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    void deleteById(Long id);
}
