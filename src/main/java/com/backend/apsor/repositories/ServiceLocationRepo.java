package com.backend.apsor.repositories;

import com.backend.apsor.entities.ServiceLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceLocationRepo extends JpaRepository<ServiceLocation, Long> {
}
