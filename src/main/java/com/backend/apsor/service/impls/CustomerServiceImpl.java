package com.backend.apsor.service.impls;

import com.backend.apsor.entities.*;
import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.enums.MediaPurpose;
import com.backend.apsor.exceptions.ApiException;
import com.backend.apsor.mapper.CustomerMapper;
import com.backend.apsor.mapper.CustomerMediaAssetMapper;
import com.backend.apsor.mapper.ProviderMediaAssetMapper;
import com.backend.apsor.payloads.dtos.CustomerDTO;
import com.backend.apsor.payloads.dtos.CustomerMediaDTO;
import com.backend.apsor.payloads.dtos.ProviderMediaDTO;
import com.backend.apsor.payloads.requests.CustomerReq;
import com.backend.apsor.repositories.CustomerMediaAssetRepo;
import com.backend.apsor.repositories.CustomerRepo;
import com.backend.apsor.repositories.MediaAssetRepo;
import com.backend.apsor.service.CustomerService;
import com.backend.apsor.service.MediaStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.backend.apsor.util.MediaUtils.*;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final UserServiceImpl userServiceImpl;
    private final CustomerMapper customerMapper;
    private final CustomerRepo customerRepo;
    private final MediaStorage mediaStorage;
    private final StorageProps storageProps;
    private final MediaAssetRepo mediaAssetRepo;
    private final CustomerMediaAssetMapper customerMediaAssetMapper;
    private final CustomerMediaAssetRepo customerMediaAssetRepo;


    @Override
    public CustomerDTO createNewCustomer(Jwt jwt, CustomerReq req) {

        Users user = userServiceImpl.loadUserByJwt(jwt);

        if (customerRepo.existsByUser_Id(user.getId())) {
            throw ApiException.conflict(ApiErrorCode.CUSTOMER_ALREADY_EXISTS,
                    "Customer profile already exists for this user id: ",
                    user.getId());
        }

        Customer customer = customerMapper.toEntity(req);
        customer.setUser(user);

        return customerMapper.toDto(customerRepo.save(customer));
    }

    @Override
    public List<CustomerDTO> getAllCustomer() {
        return customerMapper.toListDto(customerRepo.findAll());
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        return customerRepo.findById(id)
                .map(customerMapper::toDto)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Customer with id " + id + " not found."
                ));
    }
    @Override
    public CustomerDTO getCustomerByJwt(Jwt jwt) {
        Users user = userServiceImpl.loadUserByJwt(jwt);

        return customerRepo.findById(user.getId())
                .map(customerMapper::toDto)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Customer with id " + user.getId() + " not found."
                ));
    }

    @Override
    public CustomerDTO updateCustomerByJwt(Jwt jwt, CustomerReq req) {
        Users user = userServiceImpl.loadUserByJwt(jwt);
        return customerRepo.findById(user.getId())
                .map(customer -> {
                    customerMapper.updateEntity(req,customer);
                    return customerMapper.toDto(customerRepo.save(customer));
                }).orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Customer with id " + user.getId() + " not found."
                ));
    }

    @Override
    public CustomerDTO updateCustomerById(Long id, CustomerReq req) {
        return customerRepo.findById(id)
                .map(customer -> {
                     customerMapper.updateEntity(req,customer);
                     return customerMapper.toDto(customerRepo.save(customer));
                }).orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Customer with id " + id + " not found."
                ));
    }

    @Override
    public String deleteCustomerById(Long id) {
        return customerRepo.findById(id)
                .map(customer -> {
                    customerRepo.delete(customer);
                    return "Customer with id " + id + " has been deleted.";
                }).orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Customer with id " + id + " not found."
                ));
    }

    @Override
    public CustomerMediaDTO uploadNewAvatarFromAdmin(Long customerId, MultipartFile file) {
        validateAvatar(file);

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CUSTOMER_NOT_FOUND,"Customer with id " + customerId + " not found."));

        String contentType = getSafeContentType(file);
        String extension = getFileExtension(contentType);
        String objectKey = generateObjectKey(customerId, extension);

        MediaStorage.PutResult putResult = uploadToStorage(mediaStorage,objectKey, file);

        MediaAsset asset = createAndSaveMediaAsset(mediaAssetRepo, storageProps, objectKey, putResult);

        CustomerMediaAsset customerMediaAsset = CustomerMediaAsset.builder()
                .customer(customer)
                .media(asset)
                .purpose(MediaPurpose.AVATAR)
                .sortOrder(0)
                .build();

        return customerMediaAssetMapper.toDto(customerMediaAsset);
    }

    @Override
    public CustomerMediaDTO getAvatarByIdFromAdmin(Long customerId) {
        // 1) ensure provider exists
        customerRepo.findById(customerId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Customer with id " + customerId + " not found."
                ));

        // 2) pick the "best" avatar: primary first, then lowest sortOrder, then id
        CustomerMediaAsset customerMediaAsset = customerMediaAssetRepo
                .findTopByCustomer_IdAndPurposeOrderBySortOrderDescIdDesc(customerId,MediaPurpose.AVATAR)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Customer with id " + customerId + " not found."
                ));

        return customerMediaAssetMapper.toDto(customerMediaAsset);
    }

    @Override
    public CustomerMediaDTO updateByIdFromAdmin(Long customerId, MultipartFile file) {
        validateAvatar(file);

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CUSTOMER_NOT_FOUND,"Customer with id " + customerId + " not found."));

        String contentType = getSafeContentType(file);
        String extension = getFileExtension(contentType);
        String objectKey = generateObjectKey(customerId, extension);

        MediaStorage.PutResult putResult = uploadToStorage(mediaStorage,objectKey, file);

        MediaAsset asset = createAndSaveMediaAsset(mediaAssetRepo, storageProps, objectKey, putResult);

        CustomerMediaAsset customerMediaAsset = CustomerMediaAsset.builder()
                .customer(customer)
                .media(asset)
                .purpose(MediaPurpose.AVATAR)
                .sortOrder(0)
                .build();

        return customerMediaAssetMapper.toDto(customerMediaAsset);
    }

    @Override
    public String deleteAvatarByMediaIdFromAdmin(Long customerId,Long mediaId) {
        if (!customerRepo.existsById(customerId)) {
            throw ApiException.notFound(ApiErrorCode.CUSTOMER_NOT_FOUND,
                    "Customer not found: %s", customerId);
        }

        CustomerMediaAsset customerMediaAsset = customerMediaAssetRepo
                .findByCustomerIdAndPurposeAndMediaId(customerId,MediaPurpose.AVATAR,mediaId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CUSTOMER_NOT_FOUND,
                        "Customer with id " + customerId + " not found."
                ));

        MediaAsset media = customerMediaAsset.getMedia();

        if(media == null){
            throw ApiException.conflict(ApiErrorCode.MEDIA_ASSET_NOT_FOUND,
                    "Media link exists but media is missing: customerId=%s mediaId=%s",
                    customerId, mediaId);
        }

        // delete object in storage
        if (media.getObjectKey() != null && !media.getObjectKey().isBlank()) {
            mediaStorage.delete(media.getObjectKey());
        }

        customerMediaAssetRepo.delete(customerMediaAsset);

        mediaAssetRepo.delete(media);

        return "Successfully deleted customer avatar";
    }

    @Override
    public ProviderMediaDTO uploadNewAvatar(Jwt jwt, MultipartFile file) {
        return null;
    }


}
