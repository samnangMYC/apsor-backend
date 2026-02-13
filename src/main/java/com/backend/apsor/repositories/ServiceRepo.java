package com.backend.apsor.repositories;

import com.backend.apsor.entities.Services;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceRepo extends JpaRepository<Services,Long> {
    Optional<Services> findByTitle(String title);
}
