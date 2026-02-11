package com.backend.apsor.repositories;

import com.backend.apsor.entities.CustomerMediaAsset;
import com.backend.apsor.enums.MediaPurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerMediaAssetRepo extends JpaRepository<CustomerMediaAsset, Long> {
     Optional<CustomerMediaAsset> findTopByCustomer_IdAndPurposeOrderBySortOrderDescIdDesc(Long customerId, MediaPurpose mediaPurpose);

    Optional<CustomerMediaAsset> findByCustomerIdAndPurposeAndMediaId(Long customerId, MediaPurpose mediaPurpose, Long mediaId);
}
