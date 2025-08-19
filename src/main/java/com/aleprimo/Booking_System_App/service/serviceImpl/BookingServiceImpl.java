package com.aleprimo.Booking_System_App.service.serviceImpl;


import com.aleprimo.Booking_System_App.entity.Booking;
import com.aleprimo.Booking_System_App.entity.enums.BookingStatus;
import com.aleprimo.Booking_System_App.persistence.BookingDAO;
import com.aleprimo.Booking_System_App.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingDAO bookingDAO;

    @Override
    public Booking createBooking(Booking booking) {
        return bookingDAO.save(booking);
    }

    @Override
    public Booking updateBooking(Long id, Booking booking) {
        Booking booking1 = bookingDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        booking1.setCustomer(booking.getCustomer());
        booking1.setStatus(booking.getStatus());
        booking1.setBookingDateTime(booking.getBookingDateTime());
        booking1.setOffering(booking.getOffering());


        return bookingDAO.save(booking1);
    }

    @Override

    public Booking updateBookingStatus(Long id, BookingStatus status) {
        Booking booking = bookingDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        booking.setStatus(status);
        return bookingDAO.save(booking);
    }

    @Override

    public void deleteBooking(Long id) {
        bookingDAO.deleteById(id);
    }

    @Override

    public Optional<Booking> getBookingById(Long id) {
        return bookingDAO.findById(id);
    }

    @Override

    public Page<Booking> getAllBookings(Pageable pageable) {
        return bookingDAO.findAll(pageable);
    }

    @Override

    public Page<Booking> getBookingsByCustomerId(Long customerId, Pageable pageable) {
        return bookingDAO.findByCustomerId(customerId, pageable);
    }

    @Override

    public Page<Booking> getBookingsByOfferingId(Long offeringId, Pageable pageable) {
        return bookingDAO.findByOfferingId(offeringId, pageable);
    }

    @Override

    public Page<Booking> getBookingsByStatus(BookingStatus status, Pageable pageable) {
        return bookingDAO.findByStatus(status, pageable);
    }

    @Override

    public Page<Booking> getBookingsBetween(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return bookingDAO.findByBookingDateTimeBetween(start, end, pageable);
    }
}
