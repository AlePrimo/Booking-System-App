package com.aleprimo.Booking_System_App.persistence;



import com.aleprimo.Booking_System_App.entity.Offering;

import java.util.List;
import java.util.Optional;

public interface OfferingDAO {
    Offering save(Offering offering);
    Optional<Offering> findById(Long id);
    List<Offering> findAll();
    List<Offering> findByProviderId(Long providerId);
    void deleteById(Long id);
}
