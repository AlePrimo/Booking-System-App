package com.aleprimo.Booking_System_App.persistence;

import com.aleprimo.Booking_System_App.entity.Offering;
import com.aleprimo.Booking_System_App.persistence.daoImpl.OfferingDAOImpl;
import com.aleprimo.Booking_System_App.repository.OfferingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferingDAOImplTest {

    @Mock
    private OfferingRepository offeringRepository;

    @InjectMocks
    private OfferingDAOImpl offeringDAO;

    private Offering offering;

    @BeforeEach
    void setUp() {
        offering = new Offering();
        offering.setId(1L);
    }

    @Test
    void save_ShouldReturnSavedOffering() {
        when(offeringRepository.save(offering)).thenReturn(offering);

        Offering result = offeringDAO.save(offering);

        assertThat(result).isEqualTo(offering);
    }

    @Test
    void findById_ShouldReturnOffering() {
        when(offeringRepository.findById(1L)).thenReturn(Optional.of(offering));

        Optional<Offering> result = offeringDAO.findById(1L);

        assertThat(result).isPresent();
    }

    @Test
    void findAll_ShouldReturnAllOfferings() {
        when(offeringRepository.findAll()).thenReturn(List.of(offering));

        List<Offering> result = offeringDAO.findAll();

        assertThat(result).contains(offering);
    }

    @Test
    void findByProviderId_ShouldReturnOfferings() {
        when(offeringRepository.findByProviderId(100L)).thenReturn(List.of(offering));

        List<Offering> result = offeringDAO.findByProviderId(100L);

        assertThat(result).contains(offering);
    }

    @Test
    void deleteById_ShouldInvokeRepository() {
        offeringDAO.deleteById(1L);
        verify(offeringRepository, times(1)).deleteById(1L);
    }
}
