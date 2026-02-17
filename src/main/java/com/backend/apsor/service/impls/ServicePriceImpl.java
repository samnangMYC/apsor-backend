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
        Users user = userServiceImpl.loadUserByJwt(jwt);

        // find exist provider
        providerRepo.findByUser(user)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "Provider not found with " + user.getUsername()
                ));
        Services service = serviceRepo.findById(serviceId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SERVICE_NOT_FOUND,
                        "Service not found with " + serviceId
                        ));

        ServicePrice servicePrice = servicePriceMapper.toEntity(req);
        servicePrice.setService(service);
        servicePrice.setStatus(ServicePriceStatus.ACTIVE);

        return servicePriceMapper.toDTO(servicePriceRepo.save(servicePrice));
    }

    @Override
    public List<ServicePriceDTO> getAllServicePriceByServiceId(Long serviceId) {
        Services service = serviceRepo.findById(serviceId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SERVICE_NOT_FOUND,
                        "Service not found with " + serviceId
                ));

        return servicePriceMapper.toListDTO(servicePriceRepo.findAll());
    }
}
