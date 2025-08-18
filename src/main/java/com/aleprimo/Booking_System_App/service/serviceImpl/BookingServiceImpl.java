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
    @Operation(summary = "Crear una nueva reserva")
    public Booking createBooking(Booking booking) {
        return bookingDAO.save(booking);
    }

    @Override
    @Operation(summary = "Actualizar el estado de una reserva")
    public Booking updateBookingStatus(Long id, BookingStatus status) {
        Booking booking = bookingDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        booking.setStatus(status);
        return bookingDAO.save(booking);
    }

    @Override
    @Operation(summary = "Eliminar una reserva")
    public void deleteBooking(Long id) {
        bookingDAO.deleteById(id);
    }

    @Override
    @Operation(summary = "Obtener una reserva por ID")
    public Optional<Booking> getBookingById(Long id) {
        return bookingDAO.findById(id);
    }

    @Override
    @Operation(summary = "Obtener todas las reservas con paginación")
    public Page<Booking> getAllBookings(Pageable pageable) {
        return bookingDAO.findAll(pageable);
    }

    @Override
    @Operation(summary = "Obtener reservas por cliente con paginación")
    public Page<Booking> getBookingsByCustomerId(Long customerId, Pageable pageable) {
        return bookingDAO.findByCustomerId(customerId, pageable);
    }

    @Override
    @Operation(summary = "Obtener reservas por servicio con paginación")
    public Page<Booking> getBookingsByOfferingId(Long offeringId, Pageable pageable) {
        return bookingDAO.findByOfferingId(offeringId, pageable);
    }

    @Override
    @Operation(summary = "Obtener reservas por estado con paginación")
    public Page<Booking> getBookingsByStatus(BookingStatus status, Pageable pageable) {
        return bookingDAO.findByStatus(status, pageable);
    }

    @Override
    @Operation(summary = "Obtener reservas entre fechas con paginación")
    public Page<Booking> getBookingsBetween(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return bookingDAO.findByBookingDateTimeBetween(start, end, pageable);
    }
}
