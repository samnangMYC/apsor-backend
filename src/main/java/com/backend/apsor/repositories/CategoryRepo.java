package com.backend.apsor.repositories;

import com.backend.apsor.entities.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    Boolean existsByNameIgnoreCase(@NotBlank @Size(min = 2, max = 120) String name);
}
