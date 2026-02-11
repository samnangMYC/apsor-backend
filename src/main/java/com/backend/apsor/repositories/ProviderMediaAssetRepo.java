package com.backend.apsor.repositories;

import com.backend.apsor.entities.ProviderMediaAsset;
import com.backend.apsor.enums.MediaPurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderMediaAssetRepo extends JpaRepository<ProviderMediaAsset, Long> {

    Optional<ProviderMediaAsset> findTopByProvider_IdAndPurposeOrderBySortOrderDescIdDesc(Long providerId, MediaPurpose mediaPurpose);

    Optional<ProviderMediaAsset> findByProviderIdAndPurposeAndMediaId(Long providerId, MediaPurpose mediaPurpose, Long mediaId);


    Optional<ProviderMediaAsset> findByProviderIdAndMediaId(Long id, Long mediaId);

    boolean existsByMediaId(Long mediaId);
}
