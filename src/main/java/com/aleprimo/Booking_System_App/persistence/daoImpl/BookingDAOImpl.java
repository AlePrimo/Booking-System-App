package com.aleprimo.Booking_System_App.persistence.daoImpl;


import com.aleprimo.Booking_System_App.entity.Booking;
import com.aleprimo.Booking_System_App.entity.enums.BookingStatus;
import com.aleprimo.Booking_System_App.persistence.BookingDAO;
import com.aleprimo.Booking_System_App.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookingDAOImpl implements BookingDAO {

    private final BookingRepository bookingRepository;

    @Override
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public Page<Booking> findAll(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }

    @Override
    public Page<Booking> findByCustomerId(Long customerId, Pageable pageable) {
        return bookingRepository.findByCustomerId(customerId, pageable);
    }

    @Override
    public Page<Booking> findByOfferingId(Long offeringId, Pageable pageable) {
        return bookingRepository.findByOfferingId(offeringId, pageable);
    }

    @Override
    public Page<Booking> findByStatus(BookingStatus status, Pageable pageable) {
        return bookingRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<Booking> findByBookingDateTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return bookingRepository.findByBookingDateTimeBetween(start, end, pageable);
    }

    @Override
    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }
}
