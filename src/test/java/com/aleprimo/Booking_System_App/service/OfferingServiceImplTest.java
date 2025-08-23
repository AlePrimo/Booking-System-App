package com.aleprimo.Booking_System_App.service;


import com.aleprimo.Booking_System_App.entity.Offering;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import com.aleprimo.Booking_System_App.persistence.OfferingDAO;
import com.aleprimo.Booking_System_App.service.serviceImpl.OfferingServiceImpl;
import com.aleprimo.Booking_System_App.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferingServiceImplTest {

    @Mock
    private OfferingDAO offeringDAO;

    @InjectMocks
    private OfferingServiceImpl offeringService;
    @InjectMocks
    private UserServiceImpl userService;

    private Offering offering;
    private User provider;


    @BeforeEach
    void setUp() {
        provider = User.builder()
                .name("VANDALAY")
                .email("vandalay@mail")
                .password("vandalay124")
                .roles(Set.of(Role.PROVIDER))
                .build();

        offering = Offering.builder()
                .id(1L)
                .name("Service Test")
                .description("Desc")
                .durationMinutes(15)
                .provider(provider)
                .price(BigDecimal.valueOf(200.0))
                .build();
    }

    @Test
    void testCreateOffering() {
        when(offeringDAO.save(offering)).thenReturn(offering);

        Offering saved = offeringService.createOffering(offering);

        assertThat(saved.getName()).isEqualTo("Service Test");
        verify(offeringDAO, times(1)).save(offering);
    }

    @Test
    void testGetOfferingById() {
        when(offeringDAO.findById(1L)).thenReturn(Optional.of(offering));

        Optional<Offering> found = offeringService.getOfferingById(1L);

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(1L);
    }

    @Test
    void testDeleteOffering() {
        offeringService.deleteOffering(1L);

        verify(offeringDAO, times(1)).deleteById(1L);
    }
}