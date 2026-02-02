package com.backend.apsor.service;

import com.backend.apsor.enums.Status;
import com.backend.apsor.payloads.dtos.CategoryDTO;
import com.backend.apsor.payloads.requests.CategoryReq;
import com.backend.apsor.payloads.requests.CategoryStatusReq;
import jakarta.validation.Valid;

import java.util.List;

public interface CategoryService {

    CategoryDTO createNewCategory(@Valid CategoryReq req);

    List<CategoryDTO> getAllCategory();

    CategoryDTO updateCategoryById(Long id, @Valid CategoryReq req);

    String deleteCategoryById(Long id);

    CategoryDTO getCategoryById(Long id);

    String softDeleteById(Long id);

    CategoryDTO updateStatusById(Long id, CategoryStatusReq status);
}
