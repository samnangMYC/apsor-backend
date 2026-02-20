package com.backend.apsor.repositories;

import com.backend.apsor.entities.ServiceAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceAvailabilityRepo extends JpaRepository<ServiceAvailability, Long> {
    List<ServiceAvailability> findByServiceId(Long id);
}
