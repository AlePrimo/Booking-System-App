package com.aleprimo.Booking_System_App.controller;

import com.aleprimo.Booking_System_App.controller.offering.OfferingController;
import com.aleprimo.Booking_System_App.dto.offering.OfferingRequestDTO;
import com.aleprimo.Booking_System_App.dto.offering.OfferingResponseDTO;
import com.aleprimo.Booking_System_App.entity.Offering;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import com.aleprimo.Booking_System_App.mapper.offering.OfferingMapper;

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
import java.util.List;
import java.util.Optional;
import java.util.Set;


import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OfferingController.class)
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"ADMIN"})
class OfferingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OfferingService offeringService;

     @MockitoBean
     private UserService userService;

    @MockitoBean
    private OfferingMapper offeringMapper;

    private Offering offering;
    private OfferingRequestDTO requestDTO;
    private OfferingResponseDTO responseDTO;
    private User provider;

    @BeforeEach
    void setUp() {

        provider = User.builder()
                .id(1L)
                .name("VANDALAY")
                .email("vandalay@mail")
                .password("vandalay124")
                .role(Role.ROLE_PROVIDER)
                .build();
        userService.createUser(provider);


        offering = Offering.builder()
                .id(1L)
                .name("Test Offering")
                .description("Desc")
                .provider(provider)
                .durationMinutes(15)
                .price(BigDecimal.valueOf(120.0))
                .build();

        requestDTO = OfferingRequestDTO.builder()
                .name("Test Offering")
                .description("Desc")
                .price(BigDecimal.valueOf(120.0))
                .durationMinutes(15)
                .providerId(provider.getId())
                .build();

        responseDTO = OfferingResponseDTO.builder()
                .id(1L)
                .name("Test Offering")
                .description("Desc")
                .price(BigDecimal.valueOf(120.0))
                .build();
    }

    @Test
    void testCreateOffering() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(provider));
        when(offeringMapper.toEntity(any(OfferingRequestDTO.class), eq(provider))).thenReturn(offering);
        when(offeringService.createOffering(any(Offering.class))).thenReturn(offering);
        when(offeringMapper.toDTO(any(Offering.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/offerings")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }


    @Test
    void testUpdateOffering() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(provider));
        when(offeringMapper.toEntity(any(OfferingRequestDTO.class), eq(provider))).thenReturn(offering);
        when(offeringService.updateOffering(eq(1L), any(Offering.class))).thenReturn(offering);
        when(offeringMapper.toDTO(any(Offering.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/offerings/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }


    @Test
    void testDeleteOffering() throws Exception {
        mockMvc.perform(delete("/api/offerings/1").with(csrf()))
                .andExpect(status().isNoContent());
        verify(offeringService, times(1)).deleteOffering(1L);
    }




    @Test
    void testGetOfferingById() throws Exception {
        when(offeringService.getOfferingById(1L)).thenReturn(Optional.of(offering));
        when(offeringMapper.toDTO(any(Offering.class))).thenReturn(responseDTO);

        mockMvc.perform(get("/api/offerings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Offering"));
    }




    @Test
    void testGetAllOfferings() throws Exception {
        when(offeringService.getAllOfferings(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(offering), PageRequest.of(0, 10), 1));
        when(offeringMapper.toDTO(any(Offering.class))).thenReturn(responseDTO);

        mockMvc.perform(get("/api/offerings?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Test Offering"));
    }

    @Test
    void testCreateOffering_UserNotFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/offerings")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateOffering_UserNotFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/offerings/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }


}