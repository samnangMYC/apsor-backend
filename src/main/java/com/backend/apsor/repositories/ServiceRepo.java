package com.backend.apsor.repositories;

import com.backend.apsor.entities.ServiceAvailability;
import com.backend.apsor.entities.Services;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepo extends JpaRepository<Services,Long> {
    Optional<Services> findByTitle(String title);

    Optional<Services> findByIdAndPricesId(Long serviceId,Long servicePriceId);

}
