package com.backend.apsor.repositories;

import com.backend.apsor.entities.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepo extends JpaRepository<SubCategory, Long> {

    boolean existsByCategory_IdAndNameIgnoreCase(Long categoryId, String name);
}
