package com.backend.apsor.util;

import com.backend.apsor.entities.MediaAsset;
import com.backend.apsor.entities.StorageProps;
import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.enums.MediaType;
import com.backend.apsor.enums.MediaVisibility;
import com.backend.apsor.exceptions.ApiException;
import com.backend.apsor.repositories.MediaAssetRepo;
import com.backend.apsor.service.MediaStorage;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@NoArgsConstructor
public class MediaUtils {
    private static final long MAX_AVATAR_BYTES = 5_000_000L;
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/png", "image/jpeg", "image/webp");

    public static void validateAvatar(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw ApiException.badRequest(ApiErrorCode.INVALID_FILE, "File is required");
        }
        long fileSize = file.getSize();
        if (fileSize <= 0 || fileSize > MAX_AVATAR_BYTES) {
            throw ApiException.badRequest(
                    ApiErrorCode.INVALID_FILE,
                    "Avatar size invalid. Must be between 1 and %d bytes", MAX_AVATAR_BYTES
            );
        }
        String contentType = getSafeContentType(file);
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw ApiException.badRequest(
                    ApiErrorCode.INVALID_FILE,
                    "Unsupported content type: %s (allowed: png, jpeg, webp)", contentType
            );
        }
    }
    public static String getSafeContentType(MultipartFile file) {
        String contentType = file.getContentType();
        return (contentType == null || contentType.isBlank()) ? "application/octet-stream" : contentType;
    }

    public static String getFileExtension(String contentType) {
        return switch (contentType) {
            case "image/png" -> "png";
            case "image/webp" -> "webp";
            default -> "jpg"; // Fallback for image/jpeg and others
        };
    }

    public static String generateObjectKey(Long Id, String extension) {
        if (Id == null || extension == null || extension.isBlank()) {
            throw new IllegalArgumentException("ID and extension are required");
        }
        return "providers/%d/avatar/%s.%s".formatted(Id, UUID.randomUUID(), extension);
    }

    public static MediaStorage.PutResult uploadToStorage(MediaStorage mediaStorage, String objectKey, MultipartFile file) {
        try {
            return mediaStorage.put(objectKey, file);
        } catch (Exception e) {
            log.error("Failed to upload avatar to {}: {}", objectKey, e.getMessage(), e);
            throw ApiException.badRequest(
                    ApiErrorCode.MEDIA_UPLOAD_FAILED,
                    "Upload avatar failed: %s", e.getMessage()
            );
        }
    }
    public static MediaAsset createAndSaveMediaAsset(MediaAssetRepo mediaAssetRepo, StorageProps storageProps,
                                                     String objectKey, MediaStorage.PutResult putResult) {
        MediaAsset asset = MediaAsset.builder()
                .mediaType(MediaType.IMAGE)
                .visibility(MediaVisibility.PUBLIC)
                .bucket(storageProps.bucket())
                .objectKey(objectKey)
                .contentType(putResult.contentType())
                .sizeBytes(putResult.sizeBytes())
                .build();
        return mediaAssetRepo.save(asset);
    }

}
