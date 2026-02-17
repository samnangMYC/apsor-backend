package com.backend.apsor.service.impls;

import com.backend.apsor.entities.MediaAsset;
import com.backend.apsor.entities.Provider;
import com.backend.apsor.entities.ProviderMediaAsset;
import com.backend.apsor.entities.StorageProps;
import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.enums.MediaPurpose;
import com.backend.apsor.exceptions.ApiException;
import com.backend.apsor.mapper.ProviderMediaAssetMapper;
import com.backend.apsor.payloads.dtos.ProviderMediaDTO;
import com.backend.apsor.repositories.MediaAssetRepo;
import com.backend.apsor.repositories.ProviderMediaAssetRepo;
import com.backend.apsor.repositories.ProviderRepo;
import com.backend.apsor.service.storage.MediaStorage;
import com.backend.apsor.service.ProviderMediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.backend.apsor.util.MediaUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProviderMediaServiceImpl implements ProviderMediaService {

    private final ProviderMediaAssetRepo providerMediaAssetRepo;
    private final MediaAssetRepo mediaAssetRepo;
    private final ProviderRepo providerRepo;
    private final MediaStorage mediaStorage;
    private final StorageProps storageProps;
    private final ProviderMediaAssetMapper providerMediaAssetMapper;

    @Override
    public ProviderMediaDTO uploadNewAvatarFromAdmin(Long providerId, MultipartFile file) {
        validateAvatar(file);

        Provider provider = providerRepo.findById(providerId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "Provider not found with id: %d", providerId
                ));

        String contentType = getSafeContentType(file);
        String extension = getFileExtension(contentType);
        String objectKey = generateObjectKey(providerId, extension);

        MediaStorage.PutResult putResult = uploadToStorage(mediaStorage,objectKey, file);

        MediaAsset asset = createAndSaveMediaAsset(mediaAssetRepo, storageProps, objectKey, putResult);

        ProviderMediaAsset link = ProviderMediaAsset.builder()
                .provider(provider)
                .media(asset)
                .purpose(MediaPurpose.AVATAR)
                .sortOrder(0)
                .build();

        ProviderMediaAsset saved = providerMediaAssetRepo.save(link);

        return providerMediaAssetMapper.toDto(saved);
    }

    @Override
    public ProviderMediaDTO getAvatarByIdFromAdmin(Long providerId) {
        // 1) ensure provider exists
        providerRepo.findById(providerId).orElseThrow(() ->
                ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "Provider not found with id: %d",
                        providerId
                )
        );

        // 2) pick the "best" avatar: primary first, then lowest sortOrder, then id
        ProviderMediaAsset providerMediaAsset = providerMediaAssetRepo
                .findTopByProvider_IdAndPurposeOrderBySortOrderDescIdDesc(providerId, MediaPurpose.AVATAR)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.MEDIA_ASSET_NOT_FOUND,
                        "Avatar not found for provider id: %d",
                        providerId
                ));

        return providerMediaAssetMapper.toDto(providerMediaAsset);
    }

    @Override
    public ProviderMediaDTO updateNewAvatarFromAdmin(Long providerId, MultipartFile file) {
        validateAvatar(file);

        Provider provider = providerRepo.findById(providerId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "Provider not found with id: %d", providerId
                ));

        String contentType = getSafeContentType(file);
        String extension = getFileExtension(contentType);
        String objectKey = generateObjectKey(providerId, extension);

        MediaStorage.PutResult putResult = uploadToStorage(mediaStorage,objectKey, file);

        MediaAsset asset = createAndSaveMediaAsset(mediaAssetRepo, storageProps, objectKey, putResult);

        ProviderMediaAsset link = ProviderMediaAsset.builder()
                .provider(provider)
                .media(asset)
                .purpose(MediaPurpose.AVATAR)
                .sortOrder(0)
                .build();
        ProviderMediaAsset saved = providerMediaAssetRepo.save(link);

        return providerMediaAssetMapper.toDto(saved);
    }


}
