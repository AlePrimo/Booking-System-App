package com.aleprimo.Booking_System_App.service.serviceImpl;


import com.aleprimo.Booking_System_App.entity.Offering;
import com.aleprimo.Booking_System_App.exception.ResourceNotFoundException;
import com.aleprimo.Booking_System_App.persistence.OfferingDAO;
import com.aleprimo.Booking_System_App.service.OfferingService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferingServiceImpl implements OfferingService {

    private final OfferingDAO offeringDAO;

    @Override

    public Offering createOffering(Offering offering) {
        return offeringDAO.save(offering);
    }

    @Override

    public Offering updateOffering(Long id, Offering offering) {
        Offering existing = offeringDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        existing.setName(offering.getName());
        existing.setDescription(offering.getDescription());
        existing.setDurationMinutes(offering.getDurationMinutes());
        existing.setPrice(offering.getPrice());
        existing.setProvider(offering.getProvider());
        return offeringDAO.save(existing);
    }

    @Override

    public void deleteOffering(Long id) {
        offeringDAO.deleteById(id);
    }

    @Override

    public Optional<Offering> getOfferingById(Long id) {
        return offeringDAO.findById(id);
    }

    @Override

    public Page<Offering> getAllOfferings(Pageable pageable) {
        List<Offering> list = offeringDAO.findAll();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    @Override

    public Page<Offering> getOfferingsByProviderId(Long providerId, Pageable pageable) {
        List<Offering> list = offeringDAO.findByProviderId(providerId);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }
}
