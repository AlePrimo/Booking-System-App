package com.aleprimo.Booking_System_App.controller;

import com.aleprimo.Booking_System_App.controller.booking.BookingController;
import com.aleprimo.Booking_System_App.dto.booking.BookingRequestDTO;
import com.aleprimo.Booking_System_App.dto.booking.BookingResponseDTO;
import com.aleprimo.Booking_System_App.entity.Booking;
import com.aleprimo.Booking_System_App.entity.Offering;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.BookingStatus;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import com.aleprimo.Booking_System_App.mapper.booking.BookingMapper;

import com.aleprimo.Booking_System_App.service.BookingService;
import com.aleprimo.Booking_System_App.service.OfferingService;
import com.aleprimo.Booking_System_App.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"ADMIN"})

class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookingService bookingService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private OfferingService offeringService;

    @MockitoBean
    private BookingMapper bookingMapper;

    private User customer;
    private User provider;
    private Offering offering;
    private Booking booking;
    private BookingRequestDTO requestDTO;
    private BookingResponseDTO responseDTO;

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
                .id(1L)
                .name("VANDALAY")
                .email("vandalay@mail")
                .password("vandalay124")
                .roles(Set.of(Role.PROVIDER))
                .build();
        userService.createUser(provider);

        offering = Offering.builder()
                .id(1L)
                .name("Service Test")
                .description("Desc")
                .price(BigDecimal.valueOf(200.0))
                .durationMinutes(15)
                .provider(provider)
                .build();

        booking = Booking.builder()
                .id(1L)
                .bookingDateTime(LocalDateTime.now().plusDays(1))
                .status(BookingStatus.PENDING)
                .customer(customer)
                .offering(offering)
                .build();

        requestDTO = BookingRequestDTO.builder()
                .customerId(1L)
                .offeringId(1L)
                .bookingDateTime(LocalDateTime.now().plusDays(1))
                .status(BookingStatus.PENDING)
                .build();

        responseDTO = BookingResponseDTO.builder()
                .id(1L)
                .customerId(1L)
                .offeringId(1L)
                .bookingDateTime(requestDTO.getBookingDateTime())
                .status(BookingStatus.PENDING)
                .build();
    }

    @Test
    void testCreateBooking() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(customer));
        when(offeringService.getOfferingById(1L)).thenReturn(Optional.of(offering));
        when(bookingMapper.toEntity(any(BookingRequestDTO.class), any(User.class), any(Offering.class)))
                .thenReturn(booking);
        when(bookingService.createBooking(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toDTO(any(Booking.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/bookings")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }


    @Test
    void testUpdateBooking() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(customer));
        when(offeringService.getOfferingById(1L)).thenReturn(Optional.of(offering));
        when(bookingMapper.toEntity(any(BookingRequestDTO.class), any(User.class), any(Offering.class)))
                .thenReturn(booking);
        when(bookingService.updateBooking(eq(1L), any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toDTO(any(Booking.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/bookings/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }


    @Test
    void testDeleteBooking() throws Exception {
        doNothing().when(bookingService).deleteBooking(1L);

        mockMvc.perform(delete("/api/bookings/1").with(csrf()))
                .andExpect(status().isNoContent());
    }


    @Test
    void testUpdateBookingStatus() throws Exception {
        booking.setStatus(BookingStatus.CONFIRMED);
        responseDTO.setStatus(BookingStatus.CONFIRMED);

        when(bookingService.updateBookingStatus(1L, BookingStatus.CONFIRMED)).thenReturn(booking);
        when(bookingMapper.toDTO(any(Booking.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/bookings/1/status")
                        .with(csrf())
                        .param("status", "CONFIRMED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }


    @Test
    void testGetBookingById() throws Exception {
        when(bookingService.getBookingById(1L)).thenReturn(Optional.of(booking));
        when(bookingMapper.toDTO(any(Booking.class))).thenReturn(responseDTO);

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1L));
    }

    @Test
    void testGetAllBookings() throws Exception {
        when(bookingService.getAllBookings(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking), PageRequest.of(0, 10), 1));
        when(bookingMapper.toDTO(any(Booking.class))).thenReturn(responseDTO);

        mockMvc.perform(get("/api/bookings?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].offeringId").value(1L));
    }
}
