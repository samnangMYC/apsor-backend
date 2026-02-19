package com.backend.apsor.service.impls;

import com.backend.apsor.entities.Provider;
import com.backend.apsor.entities.ServicePrice;
import com.backend.apsor.entities.Services;
import com.backend.apsor.entities.Users;
import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.enums.ServicePriceStatus;
import com.backend.apsor.exceptions.ApiException;
import com.backend.apsor.mapper.ServicePriceMapper;
import com.backend.apsor.payloads.dtos.ServicePriceDTO;
import com.backend.apsor.payloads.requests.ServiceCreatePriceReq;
import com.backend.apsor.payloads.requests.ServiceUpdatePriceReq;
import com.backend.apsor.repositories.ProviderRepo;
import com.backend.apsor.repositories.ServicePriceRepo;
import com.backend.apsor.repositories.ServiceRepo;
import com.backend.apsor.repositories.UserRepo;
import com.backend.apsor.service.ServicePriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicePriceImpl implements ServicePriceService {

    private final ServicePriceRepo servicePriceRepo;
    private final ProviderRepo providerRepo;
    private final UserServiceImpl userServiceImpl;
    private final ServiceRepo serviceRepo;
    private final ServicePriceMapper servicePriceMapper;

    @Override
    public ServicePriceDTO createNewServicePrice(Jwt jwt,Long serviceId, ServiceCreatePriceReq req) {

        Provider provider = checkProviderExist(jwt);

        Services service = serviceRepo.findById(serviceId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SERVICE_NOT_FOUND,
                        "Service not found with " + serviceId
                        ));
        // ensure this service belongs to the provider
        if (!service.getProvider().getId().equals(provider.getId())) {
            throw ApiException.forbidden(ApiErrorCode.ACCESS_DENIED, "You don't own this service");
        }

        ServicePrice servicePrice = servicePriceMapper.toEntity(req);
        servicePrice.setService(service);
        servicePrice.setStatus(ServicePriceStatus.ACTIVE);

        return servicePriceMapper.toDTO(servicePriceRepo.save(servicePrice));
    }

    @Override
    public List<ServicePriceDTO> getAllServicePriceByServiceId(Jwt jwt,Long serviceId) {
        Users user = userServiceImpl.loadUserByJwt(jwt);

        serviceRepo.findById(serviceId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SERVICE_NOT_FOUND,
                        "Service not found with " + serviceId
                ));

        return servicePriceMapper.toListDTO(servicePriceRepo.findAll());
    }

    @Override
    public ServicePriceDTO updateServicePrice(Jwt jwt, Long serviceId, ServiceUpdatePriceReq req) {

        Provider provider = checkProviderExist(jwt);

        Services service = serviceRepo.findById(serviceId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SERVICE_NOT_FOUND,
                        "Service not found with " + serviceId
                ));
        // ensure this service belongs to the provider
        if (!service.getProvider().getId().equals(provider.getId())) {
            throw ApiException.forbidden(ApiErrorCode.ACCESS_DENIED, "You don't own this service");
        }

        ServicePrice servicePrice = new ServicePrice();

        servicePriceMapper.update(req,servicePrice);

        servicePrice.setService(service);

        return servicePriceMapper.toDTO(servicePriceRepo.save(servicePrice)) ;
    }

    @Override
    public String deleteServicePrice(Jwt jwt, Long serviceId,Long servicePriceId) {
        checkProviderExist(jwt);

        serviceRepo.findById(serviceId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SERVICE_NOT_FOUND,
                        "Service not found with " + serviceId
                ));

        return servicePriceRepo.findById(servicePriceId)
                .map(servicePrice -> {
                    servicePriceRepo.delete(servicePrice);
                    return "Successfully deleted service price";
                }).orElseThrow(() -> ApiException.notFound(
                                ApiErrorCode.SERVICE_NOT_FOUND,
                                "Service not found"));
    }

    private Provider checkProviderExist(Jwt jwt) {
        Users user = userServiceImpl.loadUserByJwt(jwt);

        // find exist provider
        return providerRepo.findByUser(user)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "Provider not found with " + user.getUsername()
                ));
    }
}
