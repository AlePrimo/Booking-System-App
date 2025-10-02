package com.aleprimo.Booking_System_App.service.serviceImpl;


import com.aleprimo.Booking_System_App.entity.Booking;
import com.aleprimo.Booking_System_App.entity.Notification;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.BookingStatus;
import com.aleprimo.Booking_System_App.entity.enums.NotificationType;
import com.aleprimo.Booking_System_App.exception.ResourceNotFoundException;
import com.aleprimo.Booking_System_App.persistence.BookingDAO;
import com.aleprimo.Booking_System_App.service.BookingService;
import com.aleprimo.Booking_System_App.service.NotificationService;
import com.aleprimo.Booking_System_App.service.UserService;
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
    private final NotificationService notificationService;
    private final UserService userService;

    public Booking createBooking(Booking booking) {


        Booking savedBooking = bookingDAO.save(booking);


        if (savedBooking.getOffering() != null && savedBooking.getOffering().getProvider() != null) {
            User provider = savedBooking.getOffering().getProvider();


            Notification emailNotification = Notification.builder()
                    .message("Nuevo pedido de reserva: " + savedBooking.getCustomer().getName())
                    .recipient(provider)
                    .type(NotificationType.EMAIL)
                    .sent(false)
                    .build();
            notificationService.createNotification(emailNotification);


            Notification smsNotification = Notification.builder()
                    .message("Nuevo pedido de reserva: " + savedBooking.getCustomer().getName())
                    .recipient(provider)
                    .type(NotificationType.SMS)
                    .sent(false)
                    .build();
            notificationService.createNotification(smsNotification);
        }

        return savedBooking;
    }

    @Override
    public Booking updateBooking(Long id, Booking booking) {
        Booking booking1 = bookingDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        booking1.setCustomer(booking.getCustomer());
        booking1.setStatus(booking.getStatus());
        booking1.setBookingDateTime(booking.getBookingDateTime());
        booking1.setOffering(booking.getOffering());

        Booking updatedBooking = bookingDAO.save(booking1);

      
        if (updatedBooking.getOffering() != null && updatedBooking.getOffering().getProvider() != null) {
            User provider = updatedBooking.getOffering().getProvider();

            Notification notification = Notification.builder()
                    .message("Reserva actualizada: " + updatedBooking.getCustomer().getName() +
                            " - Estado: " + updatedBooking.getStatus())
                    .recipient(provider)
                    .type(NotificationType.EMAIL)
                    .sent(false)
                    .build();
            notificationService.createNotification(notification);

            Notification smsNotification = Notification.builder()
                    .message("Reserva actualizada: " + updatedBooking.getCustomer().getName() +
                            " - Estado: " + updatedBooking.getStatus())
                    .recipient(provider)
                    .type(NotificationType.SMS)
                    .sent(false)
                    .build();
            notificationService.createNotification(smsNotification);
        }

        return updatedBooking;
    }
    @Override

    public Booking updateBookingStatus(Long id, BookingStatus status) {
        Booking booking = bookingDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));
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
