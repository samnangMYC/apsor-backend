package com.backend.apsor.service.impls;

import com.backend.apsor.entities.*;
import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.enums.MediaPurpose;
import com.backend.apsor.enums.Status;
import com.backend.apsor.exceptions.ApiException;
import com.backend.apsor.mapper.CategoryMediaMapper;
import com.backend.apsor.payloads.dtos.CategoryMediaDTO;
import com.backend.apsor.repositories.CategoryMediaRepo;
import com.backend.apsor.repositories.CategoryRepo;
import com.backend.apsor.repositories.MediaAssetRepo;
import com.backend.apsor.service.CategoryMediaService;
import com.backend.apsor.service.storage.MediaStorage;
import com.backend.apsor.util.MediaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static com.backend.apsor.util.MediaUtils.*;
import static com.backend.apsor.util.MediaUtils.createAndSaveMediaAsset;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryMediaServiceImpl implements CategoryMediaService {

    private final CategoryRepo categoryRepo;
    private final CategoryMediaRepo categoryMediaRepo;
    private final MediaStorage mediaStorage;
    private final MediaAssetRepo mediaAssetRepo;
    private final StorageProps storageProps;
    private final CategoryMediaMapper categoryMediaMapper;

    @Override
    public CategoryMediaDTO uploadCategoryMedia(Long categoryId, MultipartFile file) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Category not found with id" + categoryId
                ));
        MediaUtils.validateImage(file);

        String contentType = getSafeContentType(file);
        String extension = getFileExtension(contentType);
        String objectKey = "category/%d/image/%s.%s".formatted(categoryId, UUID.randomUUID(), extension);

        MediaStorage.PutResult putResult = uploadToStorage(mediaStorage,objectKey, file);

        MediaAsset asset = createAndSaveMediaAsset(mediaAssetRepo, storageProps, objectKey, putResult);

        CategoryMedia categoryMedia = CategoryMedia.builder()
                .category(category)
                .mediaPurpose(MediaPurpose.IMAGE)
                .sortOrder(1)
                .isPrimary(true)
                .status(Status.ACTIVE)
                .mediaAsset(asset)
                .build();

        categoryMediaRepo.save(categoryMedia);

        return categoryMediaMapper.toDto(categoryMedia);
    }

    @Override
    public List<CategoryMediaDTO> getAllCategoryImgById(Long categoryId) {
        if (!categoryRepo.existsById(categoryId)) {
            throw ApiException.notFound(ApiErrorCode.CATEGORY_NOT_FOUND,
                    "Category not found with id " + categoryId);
        }

        return categoryMediaMapper.toListDto(
                categoryMediaRepo.findAllByCategory_Id(categoryId)
        );

    }

    @Transactional
    @Override
    public CategoryMediaDTO updateCategoryMedia(Long categoryId, Long catMediaId, MultipartFile file) {

        // 1) Load existing media (and ensure it belongs to the category)
        CategoryMedia existing = categoryMediaRepo.findByIdAndCategory_Id(catMediaId, categoryId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Category media not found with id %d for category %d".formatted(catMediaId, categoryId)
                ));

        // 2) Validate purpose
        if (existing.getMediaPurpose() != MediaPurpose.IMAGE) {
            throw ApiException.badRequest(ApiErrorCode.INVALID_REQUEST, "Media is not an IMAGE");
        }

        // 3) Keep old asset info for cleanup
        MediaAsset oldAsset = existing.getMediaAsset();
        String oldKey = oldAsset != null ? oldAsset.getObjectKey() : null;

        // 4) Upload new file -> create new MediaAsset
        String contentType = getSafeContentType(file);
        String extension = getFileExtension(contentType);
        String newKey = "category/%d/image/%s.%s".formatted(categoryId, UUID.randomUUID(), extension);

        MediaStorage.PutResult putResult = uploadToStorage(mediaStorage, newKey, file);
        MediaAsset newAsset = createAndSaveMediaAsset(mediaAssetRepo, storageProps, newKey, putResult);

        // 5) Update existing CategoryMedia (NO new row)
        existing.setMediaAsset(newAsset);
        existing.setStatus(Status.ACTIVE);

        CategoryMedia saved = categoryMediaRepo.save(existing);

        // 6) Cleanup old storage + old asset row (only if not shared)
        if (oldKey != null && !oldKey.isBlank()) {
            safeDeleteStorageObject(oldKey);
        }
        if (oldAsset != null) {
            mediaAssetRepo.delete(oldAsset);
        }

        return categoryMediaMapper.toDto(saved);
    }

    @Override
    public String deleteCategoryImgByMediaId(Long categoryId, Long catMediaId) {
        // fetch media that belongs to this category
        CategoryMedia categoryMedia = categoryMediaRepo.findByIdAndCategory_Id(catMediaId, categoryId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Category media not found with id " + catMediaId + " for category " + categoryId
                ));


        MediaAsset asset = categoryMedia.getMediaAsset();
        String objectKey = asset.getObjectKey();

        categoryMediaRepo.delete(categoryMedia);
        mediaAssetRepo.delete(asset);

        safeDeleteStorageObject(objectKey);

        return "Delete category media successfully";
    }

    private void safeDeleteStorageObject(String objectKey) {
        try {
            mediaStorage.delete(objectKey);
        } catch (Exception e) {
            log.warn("Failed to delete objectKey={}", objectKey, e);
        }
    }
}
