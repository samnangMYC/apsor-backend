package com.backend.apsor.service.impls;

import com.backend.apsor.entities.*;
import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.enums.MediaPurpose;
import com.backend.apsor.enums.Status;
import com.backend.apsor.exceptions.ApiException;
import com.backend.apsor.mapper.ServiceMediaMapper;
import com.backend.apsor.payloads.dtos.ServiceMediaDTO;
import com.backend.apsor.repositories.MediaAssetRepo;
import com.backend.apsor.repositories.ServiceMediaRepo;
import com.backend.apsor.repositories.ServiceRepo;
import com.backend.apsor.service.ServiceMediaService;
import com.backend.apsor.service.storage.MediaStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.backend.apsor.util.MediaUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceMediaServiceImpl implements ServiceMediaService {

    private final ServiceRepo serviceRepo;
    private final ServiceMediaRepo serviceMediaRepo;
    private final MediaAssetRepo mediaAssetRepo;
    private final MediaStorage mediaStorage;
    private final StorageProps storageProps;
    private final ServiceMediaMapper serviceMediaMapper;
    private final UserServiceImpl userServiceImpl;

    @Override
    public List<ServiceMediaDTO> uploadNewGallery(Long serviceId, List<MultipartFile> files) {

        List<ServiceMediaDTO> dtos = new ArrayList<>(files.size());
        Boolean hasPrimary = serviceMediaRepo
                .existsByServiceIdAndMediaPurposeAndIsPrimaryTrue(serviceId, MediaPurpose.GALLERY);

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            validateImage(file);

            Services service = serviceRepo.findById(serviceId)
                    .orElseThrow(() -> ApiException.notFound(
                            ApiErrorCode.SERVICE_NOT_FOUND,
                            "Service not found with id: %d", serviceId
                    ));

            String contentType = getSafeContentType(file);
            String ext = getFileExtension(contentType);
            String objectKey = "services/%d/gallery/%s.%s".formatted(serviceId, UUID.randomUUID(), ext);

            MediaStorage.PutResult put = uploadToStorage(mediaStorage, objectKey, file);
            MediaAsset asset = createAndSaveMediaAsset(mediaAssetRepo, storageProps, objectKey, put);

            boolean makePrimary = !hasPrimary && i == 0;

            ServiceMedia serviceMedia = ServiceMedia.builder()
                    .service(service)
                    .mediaAsset(asset)
                    .mediaPurpose(MediaPurpose.GALLERY)
                    .isPrimary(makePrimary)
                    .status(Status.ACTIVE)
                    .sortOrder(i + 1)
                    .build();
            serviceMedia = serviceMediaRepo.save(serviceMedia);

            dtos.add(serviceMediaMapper.toDto(serviceMedia));
        }

        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceMediaDTO> getAllGallery(Jwt jwt, Long serviceId) {

        userServiceImpl.loadUserByJwt(jwt);

        // ensure service exists
        if (!serviceRepo.existsById(serviceId)) {
            throw ApiException.notFound(
                    ApiErrorCode.SERVICE_NOT_FOUND,
                    "Service not found with id: %d", serviceId
            );
        }

        return serviceMediaRepo
                .findAllByServiceIdAndMediaPurposeAndStatusOrderByIsPrimaryDescSortOrderAscIdAsc(
                        serviceId, MediaPurpose.GALLERY, Status.ACTIVE
                )
                .stream()
                .map(serviceMediaMapper::toDto)
                .toList();
    }

    @Override
    public ServiceMediaDTO setGalleryPrimary(Long serviceId, Long serviceMediaId) {

        ServiceMedia target = serviceMediaRepo.findByIdAndServiceId(serviceMediaId, serviceId)
                .orElseThrow(() -> ApiException.notFound(ApiErrorCode.SERVICE_MEDIA_NOT_FOUND,
                        "Gallery item not found id=%d serviceId=%d", serviceMediaId, serviceId));

        if (target.getMediaPurpose() != MediaPurpose.GALLERY) {
            throw ApiException.badRequest(ApiErrorCode.INVALID_REQUEST, "Not a gallery media");
        }

        // unset existing primary
        serviceMediaRepo.clearIsPrimaryForGallery(serviceId,MediaPurpose.GALLERY);

        target.setIsPrimary(true);
        target = serviceMediaRepo.save(target);

        return serviceMediaMapper.toDto(target);
    }

    @Override
    public ServiceMediaDTO updateGallerySortOrder(Long serviceId, Long serviceMediaId, Integer sortOrder) {
        if (sortOrder == null || sortOrder < 0) {
            throw ApiException.badRequest(ApiErrorCode.INVALID_REQUEST, "sortOrder must be >= 0");
        }

        ServiceMedia media = serviceMediaRepo.findByIdAndServiceId(serviceMediaId, serviceId)
                .orElseThrow(() -> ApiException.notFound(ApiErrorCode.SERVICE_MEDIA_NOT_FOUND,
                        "Gallery item not found id=%d serviceId=%d", serviceMediaId, serviceId));

        media.setSortOrder(sortOrder);
        return serviceMediaMapper.toDto(serviceMediaRepo.save(media));
    }

    @Override
    public ServiceMediaDTO replaceGalleryFile(Long serviceId, Long serviceMediaId, MultipartFile file) {
        validateImage(file);

        ServiceMedia media = serviceMediaRepo.findByIdAndServiceId(serviceMediaId, serviceId)
                .orElseThrow(() -> ApiException.notFound(ApiErrorCode.SERVICE_MEDIA_NOT_FOUND,
                        "Gallery item not found id=%d serviceId=%d", serviceMediaId, serviceId));

        MediaAsset oldAsset = media.getMediaAsset();

        String contentType = getSafeContentType(file);
        String ext = getFileExtension(contentType);
        String objectKey = "services/%d/gallery/%s.%s".formatted(serviceId, UUID.randomUUID(), ext);

        MediaStorage.PutResult put = uploadToStorage(mediaStorage, objectKey, file);
        MediaAsset newAsset = createAndSaveMediaAsset(mediaAssetRepo, storageProps, objectKey, put);

        media.setMediaAsset(newAsset);
        ServiceMedia saved = serviceMediaRepo.save(media);

        safeDeleteStorageObject(oldAsset.getObjectKey());
        mediaAssetRepo.delete(oldAsset);

        return serviceMediaMapper.toDto(saved);
    }


    @Override
    public String hardDeleteGalleryItem(Long serviceId, Long serviceMediaId) {
        ServiceMedia media = serviceMediaRepo.findByIdAndServiceId(serviceMediaId, serviceId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SERVICE_MEDIA_NOT_FOUND,
                        "Gallery item not found id=%d serviceId=%d", serviceMediaId, serviceId
                ));

        if (media.getMediaPurpose() != MediaPurpose.GALLERY) {
            throw ApiException.badRequest(ApiErrorCode.INVALID_REQUEST, "Not a gallery media");
        }

        boolean wasPrimary = Boolean.TRUE.equals(media.getIsPrimary());

        MediaAsset asset = media.getMediaAsset();
        String objectKey = asset.getObjectKey();

        // 1) delete ServiceMedia first (FK owner)
        serviceMediaRepo.delete(media);

        // 2) delete object from storage (MinIO/S3)
        safeDeleteStorageObject(objectKey);

        // 3) delete MediaAsset row only if not referenced elsewhere
        long refs = serviceMediaRepo.countByMediaAssetId(asset.getId());
        if (refs == 0) {
            mediaAssetRepo.delete(asset);
        }

        // 4) if primary deleted, pick next item as primary
        if (wasPrimary) {
            serviceMediaRepo.findFirstByServiceIdAndMediaPurposeAndStatusOrderBySortOrderAscIdAsc(
                    serviceId, MediaPurpose.GALLERY, Status.ACTIVE
            ).ifPresent(next -> {
                next.setIsPrimary(true);
                serviceMediaRepo.save(next);
            });
        }
        return "Successfully deleted gallery item";
    }

    private void safeDeleteStorageObject(String objectKey) {
        try {
            mediaStorage.delete(objectKey); // implement delete in MediaStorage
        } catch (Exception e) {
            // Don't fail the request just because storage delete failed
            // log and move on
             log.warn("Failed to delete objectKey={}", objectKey, e);
        }
    }

}
