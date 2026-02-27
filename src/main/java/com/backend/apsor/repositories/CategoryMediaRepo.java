package com.backend.apsor.repositories;

import com.backend.apsor.entities.CategoryMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryMediaRepo extends JpaRepository<CategoryMedia, Long> {

    List<CategoryMedia> findAllByCategory_Id(Long categoryId);

    Optional<CategoryMedia> findByIdAndCategory_Id(Long catMediaId, Long categoryId);
}
