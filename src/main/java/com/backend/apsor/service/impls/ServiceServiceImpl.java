package com.backend.apsor.service.impls;

import com.backend.apsor.entities.*;
import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.enums.ServiceStatus;
import com.backend.apsor.exceptions.ApiException;
import com.backend.apsor.helper.EntityGenerator;
import com.backend.apsor.mapper.ServiceMapper;
import com.backend.apsor.payloads.dtos.ServiceDTO;
import com.backend.apsor.payloads.requests.ServiceCreateReq;
import com.backend.apsor.payloads.requests.ServiceUpdateReq;
import com.backend.apsor.repositories.ProviderRepo;
import com.backend.apsor.repositories.ServiceRepo;
import com.backend.apsor.repositories.SubCategoryRepo;
import com.backend.apsor.service.ServiceService;
import com.backend.apsor.service.validation.StatusValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepo serviceRepo;
    private final ServiceMapper serviceMapper;
    private final SubCategoryRepo subCategoryRepo;
    private final UserServiceImpl userServiceImpl;
    private final StatusValidator statusValidator;
    private final ProviderRepo providerRepo;

    @Override
    public ServiceDTO createNewService(Jwt jwt, ServiceCreateReq req) {
        Users users = userServiceImpl.loadUserByJwt(jwt);
        // prevent duplicate
        serviceRepo.findByTitle(req.getTitle())
                .orElseThrow(() -> ApiException.conflict(
                        ApiErrorCode.DUPLICATE_SERVICE_RESOURCE,
                        "Service with title " + req.getTitle() + " already exists"
                ));

        // ensure provider exist
        requireProvider(users);

        // find sub-category and at
        SubCategory subCategory = requireActiveSubCategory(req.getSubCategoryId());

        // convert req to entity
        Services services = serviceMapper.toEntity(req);
        // auto creating category
        services.setPublicId(UUID.randomUUID());
        services.setProvider(users.getProvider());
        services.setSlug(EntityGenerator.generateSlug(req.getTitle()));
        services.setCategory(subCategory.getCategory());
        services.setPublishedAt(Instant.now());
        services.setStatus(ServiceStatus.ACTIVE);
        serviceRepo.save(services);

        return serviceMapper.toDTO(services);
    }

    @Override
    public List<ServiceDTO> getAllService() {
        return serviceMapper.toDTOs(serviceRepo.findAll());
    }

    @Override
    public ServiceDTO updateService(Jwt jwt, ServiceUpdateReq req) {
        Users users = userServiceImpl.loadUserByJwt(jwt);

        // ensure provider exist
        requireProvider(users);

        // find sub-category and ensure active
        requireActiveSubCategory(req.getSubCategoryId());

        // convert req to entity
        Services services = serviceMapper.toEntity(req);

        switch (req.getStatus()) {
            case ACTIVE:
                services.setStatus(ServiceStatus.ACTIVE);
                break;
            case ARCHIVED:
                services.setStatus(ServiceStatus.ARCHIVED);
                break;
            case DRAFT:
                services.setStatus(ServiceStatus.DRAFT);
                break;
            case SUSPENDED:
                services.setStatus(ServiceStatus.SUSPENDED);
                services.setSuspendedAt(Instant.now());
        }
        serviceRepo.save(services);

        return serviceMapper.toDTO(services);
    }

    @Override
    public String deleteServiceById(Jwt jwt, Long id) {
        userServiceImpl.loadUserByJwt(jwt);

        return serviceRepo.findById(id)
                .map(services -> {
                    serviceRepo.delete(services);
                    return "Successfully deleted service with id " + id;
                }).orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SERVICE_NOT_FOUND,
                        "Service not found with id " + id
                ) );
    }
    private void requireProvider(Users users) {
        providerRepo.findByUser(users)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "Provider not found"
                ));

    }

    public SubCategory requireActiveSubCategory(Long subCategoryId) {
        SubCategory subCategory = subCategoryRepo.findById(subCategoryId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SUBCATEGORY_NOT_FOUND,
                        "Sub category not found with id " + subCategoryId
                ));

        Category category = subCategory.getCategory();

        statusValidator.requireActive(
                category.getStatus(),
                ApiErrorCode.CATEGORY_INACTIVE,
                "Category",
                category.getId()
        );

        statusValidator.requireActive(
                subCategory.getStatus(),
                ApiErrorCode.SUBCATEGORY_INACTIVE,
                "SubCategory",
                subCategory.getId()
        );

        return subCategory;
    }
}
