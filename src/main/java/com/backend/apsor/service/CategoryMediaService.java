package com.backend.apsor.service;

import com.backend.apsor.payloads.dtos.CategoryMediaDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryMediaService {
    CategoryMediaDTO uploadCategoryMedia(Long categoryId, MultipartFile file);

    CategoryMediaDTO updateCategoryMedia(Long categoryId, Long catMediaId, MultipartFile file);

    String deleteCategoryImgByMediaId(Long categoryId, Long catMediaId);

    List<CategoryMediaDTO> getAllCategoryImgById(Long categoryId);
}
