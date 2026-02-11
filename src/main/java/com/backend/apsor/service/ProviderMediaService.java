package com.backend.apsor.service;

import com.backend.apsor.payloads.dtos.MediaAssetDTO;
import com.backend.apsor.payloads.dtos.ProviderMediaDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ProviderMediaService {
    ProviderMediaDTO uploadNewAvatarFromAdmin(Long providerId, MultipartFile file);

    ProviderMediaDTO getAvatarByIdFromAdmin(Long providerId);

    ProviderMediaDTO updateNewAvatarFromAdmin(Long providerId, MultipartFile file);
}
