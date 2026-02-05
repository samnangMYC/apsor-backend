package com.backend.apsor.service;

import com.backend.apsor.payloads.dtos.SubCategoryDTO;
import com.backend.apsor.payloads.requests.SubCategoryReq;
import com.backend.apsor.payloads.requests.SubCategoryStatusReq;
import com.backend.apsor.payloads.requests.SubCategoryUpdateReq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public interface SubCategoryService {
    SubCategoryDTO createNewSubCategory(@Valid SubCategoryReq req);

    List<SubCategoryDTO> getAllSubCategory();

    SubCategoryDTO getSubCategoryById(Long id);

    SubCategoryDTO updateSubCategoryById(Long id, @Valid SubCategoryUpdateReq req);
    
    SubCategoryDTO updateStatusById(Long id, SubCategoryStatusReq req);

    String softDeleteById(Long id);

    String deleteCategoryById(Long id);
}
