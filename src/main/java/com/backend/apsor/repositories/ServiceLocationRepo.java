package com.backend.apsor.repositories;

import com.backend.apsor.entities.ServiceLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceLocationRepo extends JpaRepository<ServiceLocation, Long> {
    List<ServiceLocation> findByServiceId(Long id);
}
