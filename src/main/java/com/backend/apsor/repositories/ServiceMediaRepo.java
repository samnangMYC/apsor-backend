package com.backend.apsor.repositories;

import com.backend.apsor.entities.ServiceMedia;
import com.backend.apsor.enums.MediaPurpose;
import com.backend.apsor.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ServiceMediaRepo extends JpaRepository<ServiceMedia, Long> {

    List<ServiceMedia> findAllByServiceIdAndMediaPurposeAndStatusOrderByIsPrimaryDescSortOrderAscIdAsc(Long serviceId, MediaPurpose mediaPurpose, Status status);

    List<ServiceMedia> findAllByServiceId(Long serviceId);

    Boolean existsByServiceIdAndMediaPurposeAndIsPrimaryTrue(Long serviceId, MediaPurpose mediaPurpose);

    Optional<ServiceMedia> findByIdAndServiceId(Long serviceMediaId, Long serviceId);

    @Modifying
    @Query("""
        update ServiceMedia sm
        set sm.isPrimary = false
        where sm.service.id = :serviceId
          and sm.mediaPurpose = :purpose
    """)
    void clearIsPrimaryForGallery(@Param("serviceId") Long serviceId,
                                  @Param("purpose") MediaPurpose purpose);

    Long countByMediaAssetId(Long id);

    Optional<ServiceMedia> findFirstByServiceIdAndMediaPurposeAndStatusOrderBySortOrderAscIdAsc(Long serviceId, MediaPurpose mediaPurpose, Status status);
}
