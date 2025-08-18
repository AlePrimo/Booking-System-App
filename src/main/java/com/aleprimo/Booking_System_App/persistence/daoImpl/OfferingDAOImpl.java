package com.aleprimo.Booking_System_App.persistence.daoImpl;

import com.aleprimo.Booking_System_App.entity.Offering;
import com.aleprimo.Booking_System_App.persistence.OfferingDAO;

import com.aleprimo.Booking_System_App.repository.OfferingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OfferingDAOImpl implements OfferingDAO {

    private final OfferingRepository offeringRepository;

    @Override
    public Offering save(Offering offering) {
        return offeringRepository.save(offering);
    }

    @Override
    public Optional<Offering> findById(Long id) {
        return offeringRepository.findById(id);
    }

    @Override
    public List<Offering> findAll() {
        return offeringRepository.findAll();
    }

    @Override
    public List<Offering> findByProviderId(Long providerId) {
        return offeringRepository.findByProviderId(providerId);
    }

    @Override
    public void deleteById(Long id) {
        offeringRepository.deleteById(id);
    }
}
