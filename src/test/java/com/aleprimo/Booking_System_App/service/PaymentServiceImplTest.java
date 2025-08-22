package com.aleprimo.Booking_System_App.service;


import com.aleprimo.Booking_System_App.entity.Booking;
import com.aleprimo.Booking_System_App.entity.Offering;
import com.aleprimo.Booking_System_App.entity.Payment;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.BookingStatus;
import com.aleprimo.Booking_System_App.entity.enums.PaymentMethod;
import com.aleprimo.Booking_System_App.entity.enums.PaymentStatus;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import com.aleprimo.Booking_System_App.persistence.OfferingDAO;
import com.aleprimo.Booking_System_App.persistence.PaymentDAO;
import com.aleprimo.Booking_System_App.persistence.UserDAO;
import com.aleprimo.Booking_System_App.service.serviceImpl.OfferingServiceImpl;
import com.aleprimo.Booking_System_App.service.serviceImpl.PaymentServiceImpl;
import com.aleprimo.Booking_System_App.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentDAO paymentDAO;

    @Mock
    private UserDAO userDAO;
    @Mock
    private OfferingDAO offeringDAO;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @InjectMocks
    private UserServiceImpl userService;
    @InjectMocks
    private OfferingServiceImpl offeringService;

    private Booking booking;
    private Payment payment;
    private User customer;
    private Offering offering;
    private User provider;
    @BeforeEach
    void setUp() {

        customer = User.builder()
                .email("pepe@mail")
                .name("pepe")
                .password("pepe1234")
                .roles(Set.of(Role.CUSTOMER))
                .build();
        userService.createUser(customer);

        provider = User.builder()
                .roles(Set.of(Role.PROVIDER))
                .password("provider1234")
                .email("provider@mail")
                .name("vandalay")
                .build();
        userService.createUser(provider);

        offering = Offering.builder()
                .price(BigDecimal.valueOf(100.0))
                .durationMinutes(60)
                .name("plomeria")
                .provider(provider)
                .description("Destapamiento de caconia")
                .build();

        offeringService.createOffering(offering);



        booking = Booking.builder()
                .id(1L)
                .customer(customer)
                .offering(offering)
                .status(BookingStatus.PENDING)
                .build();

        payment = Payment.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(2500))
                .method(PaymentMethod.CASH)
                .status(PaymentStatus.PENDING)
                .booking(booking)
                .build();
    }

    @Test
    void testCreatePayment() {
        when(paymentDAO.save(any(Payment.class))).thenReturn(payment);
        Payment saved = paymentService.createPayment(payment);
        assertThat(saved).isNotNull();
        assertThat(saved.getAmount()).isEqualTo(BigDecimal.valueOf(2500));
        verify(paymentDAO, times(1)).save(payment);
    }

    @Test
    void testGetPaymentById() {
        when(paymentDAO.findById(1L)).thenReturn(Optional.of(payment));
        Optional<Payment> found = paymentService.getPaymentById(1L);
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(1L);
    }

    @Test
    void testGetPaymentByBookingId() {
        when(paymentDAO.findByBookingId(1L)).thenReturn(Optional.of(payment));
        Optional<Payment> found = paymentService.getPaymentByBookingId(1L);
        assertThat(found).isPresent();
        assertThat(found.get().getBooking().getId()).isEqualTo(1L);
    }

    @Test
    void testGetAllPayments() {
        Page<Payment> page = new PageImpl<>(List.of(payment));
        when(paymentDAO.findAll(PageRequest.of(0, 10))).thenReturn(page);
        Page<Payment> result = paymentService.getAllPayments(PageRequest.of(0, 10));
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void testDeletePayment() {
        doNothing().when(paymentDAO).deleteById(1L);
        paymentService.deletePayment(1L);
        verify(paymentDAO, times(1)).deleteById(1L);
    }
}
