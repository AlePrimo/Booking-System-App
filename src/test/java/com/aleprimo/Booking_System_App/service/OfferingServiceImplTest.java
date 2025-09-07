package com.aleprimo.Booking_System_App.service;



import com.aleprimo.Booking_System_App.entity.Offering;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import com.aleprimo.Booking_System_App.exception.ResourceNotFoundException;
import com.aleprimo.Booking_System_App.persistence.OfferingDAO;
import com.aleprimo.Booking_System_App.service.serviceImpl.OfferingServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferingServiceImplTest {

    @Mock
    private OfferingDAO offeringDAO;

    @InjectMocks
    private OfferingServiceImpl offeringService;

    private Offering offering;
    private User provider;

    @BeforeEach
    void setUp() {
        provider = User.builder()
                .id(10L)
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

    @Test
    void testUpdateOffering_Success() {
        Offering updated = Offering.builder()
                .id(1L)
                .name("Updated Service")
                .description("Updated Desc")
                .durationMinutes(30)
                .provider(provider)
                .price(BigDecimal.valueOf(500.0))
                .build();

        when(offeringDAO.findById(1L)).thenReturn(Optional.of(offering));
        when(offeringDAO.save(any(Offering.class))).thenReturn(updated);

        Offering result = offeringService.updateOffering(1L, updated);

        assertThat(result.getName()).isEqualTo("Updated Service");
        assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(500.0));
        verify(offeringDAO, times(1)).findById(1L);
        verify(offeringDAO, times(1)).save(any(Offering.class));
    }

    @Test
    void testUpdateOffering_NotFound() {
        Offering updated = Offering.builder()
                .id(99L)
                .name("Non-existent")
                .build();

        when(offeringDAO.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> offeringService.updateOffering(99L, updated))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Servicio no encontrado");

        verify(offeringDAO, times(1)).findById(99L);
        verify(offeringDAO, never()).save(any());
    }

    @Test
    void testGetAllOfferings() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("name"));
        List<Offering> offerings = List.of(offering);

        when(offeringDAO.findAll()).thenReturn(offerings);

        Page<Offering> page = offeringService.getAllOfferings(pageable);

        assertThat(page).isNotEmpty();
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getName()).isEqualTo("Service Test");
    }

    @Test
    void testGetOfferingsByProviderId() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Offering> offerings = List.of(offering);

        when(offeringDAO.findByProviderId(provider.getId())).thenReturn(offerings);

        Page<Offering> page = offeringService.getOfferingsByProviderId(provider.getId(), pageable);

        assertThat(page).isNotEmpty();
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getProvider().getName()).isEqualTo("VANDALAY");
    }







}