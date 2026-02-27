package com.backend.apsor.repositories;

import com.backend.apsor.entities.Category;
import com.backend.apsor.payloads.dtos.CategoryDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    Boolean existsBySlugIgnoreCase(String baseSlug);

    Optional<Category> findByIdAndCategoryMediaId(Long categoryId, Long mediaId);
}
