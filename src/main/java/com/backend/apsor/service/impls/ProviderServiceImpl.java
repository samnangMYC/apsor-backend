package com.backend.apsor.service.impls;

import com.backend.apsor.entities.*;
import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.enums.MediaPurpose;
import com.backend.apsor.enums.ProviderStatus;
import com.backend.apsor.exceptions.ApiException;
import com.backend.apsor.mapper.MediaAssetMapper;
import com.backend.apsor.mapper.ProviderMapper;
import com.backend.apsor.mapper.ProviderMediaAssetMapper;
import com.backend.apsor.payloads.dtos.AdminProviderDTO;
import com.backend.apsor.payloads.dtos.MediaAssetDTO;
import com.backend.apsor.payloads.dtos.ProviderDTO;
import com.backend.apsor.payloads.dtos.ProviderMediaDTO;
import com.backend.apsor.payloads.requests.AdminProviderReq;
import com.backend.apsor.payloads.requests.ProviderReq;
import com.backend.apsor.payloads.requests.ProviderStatusReq;
import com.backend.apsor.repositories.MediaAssetRepo;
import com.backend.apsor.repositories.ProviderMediaAssetRepo;
import com.backend.apsor.repositories.ProviderRepo;
import com.backend.apsor.repositories.UserRepo;
import com.backend.apsor.service.storage.MediaStorage;
import com.backend.apsor.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.backend.apsor.util.MediaUtils.*;
import static com.backend.apsor.util.MediaUtils.createAndSaveMediaAsset;
import static com.backend.apsor.util.MediaUtils.generateObjectKey;
import static com.backend.apsor.util.MediaUtils.uploadToStorage;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final UserRepo userRepo;
    private final ProviderRepo providerRepo;
    private final ProviderMapper providerMapper;
    private final UserServiceImpl userServiceImpl;
    private final MediaStorage mediaStorage;
    private final StorageProps storageProps;
    private final MediaAssetMapper mediaAssetMapper;
    private final ProviderMediaAssetMapper providerMediaAssetMapper;
    private final MediaAssetRepo mediaAssetRepo;
    private final ProviderMediaAssetRepo providerMediaAssetRepo;

    @Override
    public AdminProviderDTO createNewProviderByAdmin(AdminProviderReq req) {
        Users user = userRepo.findById(Long.valueOf(req.getUserId()))
                .orElseThrow(() ->  ApiException.notFound(
                        ApiErrorCode.USER_NOT_FOUND,
                        "User not found with id:",
                        req.getUserId()
                ));

        // Check if the user already has a provider (prevents duplicates)
        if (providerRepo.findByUser(user).isPresent()) {
            throw ApiException.conflict(
                    ApiErrorCode.PROVIDER_ALREADY_EXISTS,
                    "A provider already exists for user with id:",
                    req.getUserId()
            );
        }

        Provider provider = providerMapper.toEntityFromAdmin(req);
        provider.setUser(user);
        provider.setIsAvailable(Boolean.TRUE);

        return providerMapper.toAdminDto(providerRepo.save(provider));
    }

    @Override
    public List<AdminProviderDTO> getAllProviderByAdmin() {
        return providerRepo.findAll()
                .stream()
                .map(providerMapper::toAdminDto).toList();
    }

    @Override
    public AdminProviderDTO getProviderByIdFromAdmin(Long id) {
        return providerRepo.findById(id)
                .map(providerMapper::toAdminDto)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "Provider not found with id: ",
                        id.toString()
                ));
    }

    @Override
    public AdminProviderDTO updateProviderByIdFromAdmin(Long id) {
        Provider provider = providerRepo.findById(id)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "Provider not found with id: ",
                        id.toString()
                ));

        return providerMapper.toAdminDto(providerRepo.save(provider));
    }

    @Override
    public AdminProviderDTO updateStatusByIdFromAdmin(Long id, ProviderStatusReq req) {
        return providerRepo.findById(id)
                .map(provider -> {
                    provider.setStatus(req.getStatus());
                    return providerMapper.toAdminDto(providerRepo.save(provider));
                }).orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "Provider not found with id: ",
                        id.toString()
                ));
    }

    @Override
    public String deleteProviderByIdFromAdmin(Long id) {

        return providerRepo.findById(id)
                .map(provider -> {
                    providerRepo.delete(provider);
                    return "Provider has been deleted";
                }).orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "Provider not found with id: ",
                        id.toString()
                ));
    }
    @Override
    public ProviderDTO createNewProviderByJwt(Jwt jwt, ProviderReq req) {

        Users user = userServiceImpl.loadUserByJwt(jwt);

        // Check if the user already has a provider (prevents duplicates)
        if (providerRepo.findByUser(user).isPresent()) {
            throw ApiException.conflict(
                    ApiErrorCode.PROVIDER_ALREADY_EXISTS,
                    "A provider already exists for user with id:",
                    user.getId().toString()
            );
        }

        Provider provider = providerMapper.toEntity(req);
        provider.setUser(user);
        provider.setStatus(ProviderStatus.ACTIVE);
        provider.setIsAvailable(Boolean.TRUE);

        return providerMapper.toDTO(providerRepo.save(provider));
    }

    @Override
    public ProviderDTO getProviderByJwt(Jwt jwt) {
        Users user = userServiceImpl.loadUserByJwt(jwt);
        return providerRepo.findByUser(user)
                .map(providerMapper::toDTO)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "Provider not found with id:",
                        user.getId().toString()
                ));
    }

    @Override
    public ProviderDTO updateProviderByJwt(Jwt jwt, ProviderReq req) {
        Users user = userServiceImpl.loadUserByJwt(jwt);

        Provider provider = providerRepo.findByUser_Id(user.getId())
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "Provider not found for user id: %d",
                        user.getId()
                ));

        // update only mutable fields
        providerMapper.update(provider, req);

        // IMPORTANT if your Provider uses @MapsId: ensure user is not null
        provider.setUser(user);

        return providerMapper.toDTO(providerRepo.save(provider));
    }

    @Override
    public ProviderMediaDTO uploadNewAvatar(Jwt jwt, MultipartFile file) {
        validateAvatar(file);

        Users user = userServiceImpl.loadUserByJwt(jwt);

        Provider provider = providerRepo.findByUser(user)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "Provider not found with id: %d",
                        user.getId().toString()
                ));

        String contentType = getSafeContentType(file);
        String extension = getFileExtension(contentType);
        String objectKey = generateObjectKey(provider.getId(), extension);

        MediaStorage.PutResult putResult = uploadToStorage(mediaStorage,objectKey, file);

        MediaAsset asset = createAndSaveMediaAsset(mediaAssetRepo, storageProps, objectKey, putResult);

        ProviderMediaAsset link = ProviderMediaAsset.builder()
                .provider(provider)
                .media(asset)
                .purpose(MediaPurpose.AVATAR)
                .sortOrder(0)
                .build();

        return providerMediaAssetMapper.toDto(link);
    }

    @Override
    public ProviderMediaDTO getAvatarByJwt(Jwt jwt) {

        Users user = userServiceImpl.loadUserByJwt(jwt);

        // 1) ensure provider exists
        Provider provider = providerRepo.findByUser(user).orElseThrow(() ->
                ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "User not found with id: %d",
                        user.getId().toString()
                )
        );

        // 2) pick the "best" avatar: primary first, then lowest sortOrder, then id
        ProviderMediaAsset providerMediaAsset = providerMediaAssetRepo
                .findTopByProvider_IdAndPurposeOrderBySortOrderDescIdDesc(provider.getId(), MediaPurpose.AVATAR)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.MEDIA_ASSET_NOT_FOUND,
                        "Avatar not found for provider id: %d",
                        provider.getId().toString()
                ));

        return providerMediaAssetMapper.toDto(providerMediaAsset);
    }

    @Override
    public ProviderMediaDTO updateProviderAvatar(Jwt jwt, MultipartFile file) {

        validateAvatar(file);

        Users user = userServiceImpl.loadUserByJwt(jwt);

        Provider provider = providerRepo.findByUser(user)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "Provider not found with id: %d",
                        user.getId().toString()
                ));

        String contentType = getSafeContentType(file);
        String extension = getFileExtension(contentType);
        String objectKey = generateObjectKey(provider.getId(), extension);

        MediaStorage.PutResult putResult = uploadToStorage(mediaStorage,objectKey, file);

        MediaAsset asset = createAndSaveMediaAsset(mediaAssetRepo, storageProps, objectKey, putResult);

        ProviderMediaAsset link = ProviderMediaAsset.builder()
                .provider(provider)
                .media(asset)
                .purpose(MediaPurpose.AVATAR)
                .sortOrder(0)
                .build();

        return providerMediaAssetMapper.toDto(link);
    }

    @Override
    public String deleteAvatarByIdFromAdmin(Long providerId, Long mediaId) {
        // ensure provider exists
        if (!providerRepo.existsById(providerId)) {
            throw ApiException.notFound(ApiErrorCode.PROVIDER_NOT_FOUND,
                    "Provider not found: %s", providerId);
        }

        ProviderMediaAsset providerMediaAsset = providerMediaAssetRepo.
                findByProviderIdAndPurposeAndMediaId(providerId,MediaPurpose.AVATAR,mediaId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "Provider not found: %s", providerId
                ));


        // fetch media AND verify it belongs to provider
        MediaAsset media = providerMediaAsset.getMedia();

        if(media == null){
            throw ApiException.conflict(ApiErrorCode.MEDIA_ASSET_NOT_FOUND,
                    "Media link exists but media is missing: providerId=%s mediaId=%s",
                    providerId, mediaId);
        }

        // delete object in storage
        if (media.getObjectKey() != null && !media.getObjectKey().isBlank()) {
            mediaStorage.delete(media.getObjectKey());
        }

        providerMediaAssetRepo.delete(providerMediaAsset);

        // delete db row
        mediaAssetRepo.delete(media);
        return "Successfully delete avatar";
    }

    @Override
    public String deleteAvatarByMediaId(Jwt jwt, Long mediaId) {
        Users user = userServiceImpl.loadUserByJwt(jwt);

        Provider provider = providerRepo.findByUser(user)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "Provider not found for current user"
                ));

        ProviderMediaAsset providerMediaAsset = providerMediaAssetRepo
                .findByProviderIdAndPurposeAndMediaId(provider.getId(),MediaPurpose.AVATAR, mediaId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.MEDIA_ASSET_NOT_FOUND,
                        "Media not found for this provider"
                ));

        MediaAsset media = providerMediaAsset.getMedia();
        if (media == null) {
            throw ApiException.notFound(ApiErrorCode.MEDIA_ASSET_NOT_FOUND, "Media missing");
        }

        // delete object in storage
        String objectKey = media.getObjectKey();
        if (objectKey != null && !objectKey.isBlank()) {
            mediaStorage.delete(objectKey);
        }

        // delete join record
        providerMediaAssetRepo.delete(providerMediaAsset);

        // delete media row only if not referenced by anyone else
        if (!providerMediaAssetRepo.existsByMediaId(mediaId)){
            mediaAssetRepo.deleteById(mediaId);
        }

        return "Successfully delete avatar";
    }


}
