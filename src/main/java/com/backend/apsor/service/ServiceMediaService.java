package com.backend.apsor.service;

import com.backend.apsor.payloads.dtos.ServiceMediaDTO;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ServiceMediaService {

    List<ServiceMediaDTO> uploadNewGallery(Long serviceId, List<MultipartFile> file);

    List<ServiceMediaDTO> getAllGallery(Jwt jwt, Long serviceId);

    ServiceMediaDTO setGalleryPrimary(Long serviceId, Long serviceMediaId);

    ServiceMediaDTO updateGallerySortOrder(Long serviceId, Long serviceMediaId, Integer sortOrder);

    ServiceMediaDTO replaceGalleryFile(Long serviceId, Long serviceMediaId, MultipartFile file);

    String hardDeleteGalleryItem(Long serviceId, Long serviceMediaId);
}
