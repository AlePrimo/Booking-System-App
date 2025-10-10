package com.aleprimo.Booking_System_App.controller;


import com.aleprimo.Booking_System_App.controller.payment.PaymentController;
import com.aleprimo.Booking_System_App.dto.payment.PaymentRequestDTO;
import com.aleprimo.Booking_System_App.dto.payment.PaymentResponseDTO;
import com.aleprimo.Booking_System_App.entity.Booking;
import com.aleprimo.Booking_System_App.entity.Offering;
import com.aleprimo.Booking_System_App.entity.Payment;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.BookingStatus;
import com.aleprimo.Booking_System_App.entity.enums.PaymentMethod;
import com.aleprimo.Booking_System_App.entity.enums.PaymentStatus;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import com.aleprimo.Booking_System_App.mapper.payment.PaymentMapper;
import com.aleprimo.Booking_System_App.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"ADMIN"})
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PaymentService paymentService;

    @MockitoBean
    private BookingService bookingService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private OfferingService offeringService;

    @MockitoBean
    private PaymentMapper paymentMapper;

    @MockitoBean
    private NotificationService notificationService;

    private Booking booking;
    private Payment payment;
    private PaymentRequestDTO requestDTO;
    private PaymentResponseDTO responseDTO;
    private User customer;
    private Offering offering;
    private User provider;

    @BeforeEach
    void setUp() {

        customer = User.builder()
                .email("pepe@mail")
                .name("pepe")
                .password("pepe1234")
                .role(Role.ROLE_CUSTOMER)
                .build();
        userService.createUser(customer);

        provider = User.builder()
                .role(Role.ROLE_PROVIDER)
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

        requestDTO = PaymentRequestDTO.builder()
                .bookingId(1L)
                .amount(BigDecimal.valueOf(2500))
                .method(PaymentMethod.CASH)
                .build();

        responseDTO = PaymentResponseDTO.builder()
                .id(1L)
                .bookingId(1L)
                .amount(BigDecimal.valueOf(2500))
                .build();
    }

    @Test
    void testCreatePayment() throws Exception {
        when(bookingService.getBookingById(1L)).thenReturn(Optional.of(booking));
        when(paymentMapper.toEntity(any(PaymentRequestDTO.class), any(Booking.class))).thenReturn(payment);
        when(paymentService.createPayment(any(Payment.class))).thenReturn(payment);
        when(paymentMapper.toDTO(any(Payment.class))).thenReturn(responseDTO);
        when(notificationService.createNotification(any())).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/api/payments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.amount").value(2500));
    }
    @Test
    void testCreatePayment_BookingNotFound() throws Exception {
        // Simulamos que no existe la reserva
        when(bookingService.getBookingById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/payments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Reserva no encontrada", result.getResolvedException().getMessage()));
    }

    @Test
    void testCreatePayment_ExistingPaymentThrowsException() throws Exception {
        // Simulamos que la reserva existe
        when(bookingService.getBookingById(1L)).thenReturn(Optional.of(booking));
        // Simulamos que ya existe un pago para esa reserva
        when(paymentService.getPaymentByBookingId(1L)).thenReturn(Optional.of(payment));

        mockMvc.perform(post("/api/payments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Ya existe un pago registrado para esta reserva", result.getResolvedException().getMessage()));
    }

    @Test
    void testGetPaymentById() throws Exception {
        when(paymentService.getPaymentById(1L)).thenReturn(Optional.of(payment));
        when(paymentMapper.toDTO(payment)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1L));
    }

    @Test
    void testGetAllPayments() throws Exception {
        when(paymentService.getAllPayments(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(payment)));
        when(paymentMapper.toDTO(any(Payment.class))).thenReturn(responseDTO);

        mockMvc.perform(get("/api/payments?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].amount").value(2500));
    }

    @Test
    void testDeletePayment() throws Exception {
        mockMvc.perform(delete("/api/payments/1").with(csrf()))
                .andExpect(status().isNoContent());
    }
}
