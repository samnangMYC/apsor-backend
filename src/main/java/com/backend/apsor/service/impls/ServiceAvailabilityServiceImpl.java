package com.backend.apsor.service.impls;

import com.backend.apsor.entities.ServiceAvailability;
import com.backend.apsor.entities.Services;
import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.exceptions.ApiException;
import com.backend.apsor.mapper.ServiceAvailabilityMapper;
import com.backend.apsor.payloads.dtos.ServiceAvailabilityDTO;
import com.backend.apsor.payloads.requests.ServiceAvailabilityReq;
import com.backend.apsor.repositories.ServiceAvailabilityRepo;
import com.backend.apsor.repositories.ServiceRepo;
import com.backend.apsor.service.ServiceAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceAvailabilityServiceImpl implements ServiceAvailabilityService {

    private final ServiceAvailabilityRepo serviceAvailabilityRepo;
    private final ServiceRepo serviceRepo;
    private final ServiceAvailabilityMapper serviceAvailabilityMapper;

    @Override
    public ServiceAvailabilityDTO createNewServiceAvb(ServiceAvailabilityReq req) {
        Services services = serviceRepo.findById(req.getServiceId())
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SERVICE_NOT_FOUND,
                        "Service not found with id " + req.getServiceId()
                ));
        ServiceAvailability serviceAvailability = serviceAvailabilityMapper.toEntity(req);
        serviceAvailability.setService(services);

        return serviceAvailabilityMapper.toDto(serviceAvailabilityRepo.save(serviceAvailability));
    }


    @Override
    public ServiceAvailabilityDTO getServiceAvailabilityByAvbId(Long availabilityId) {
        return serviceAvailabilityRepo.findById(availabilityId)
                .stream().map(serviceAvailabilityMapper::toDto).findFirst()
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SERVICE_AVAILABILITY_NOT_FOUND,
                        "Service availability not found with id " + availabilityId
                ));
    }

    @Override
    public ServiceAvailabilityDTO updateServiceAvailabilityByAvbId(Long availabilityId,ServiceAvailabilityReq req) {
        return serviceAvailabilityRepo.findById(availabilityId)
                .map(sv -> {
                    serviceAvailabilityMapper.update(req,sv);
                    return serviceAvailabilityMapper.toDto(sv);
                })
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SERVICE_AVAILABILITY_NOT_FOUND,
                        "Service availability not found with id " + availabilityId
                ));
    }

    @Override
    public String deleteServiceAvailabilityByAvbId(Long availabilityId) {
        return serviceAvailabilityRepo.findById(availabilityId)
                .map(sv -> {
                    serviceAvailabilityRepo.delete(sv);
                    return "Successfully deleted service availability";
                })
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SERVICE_AVAILABILITY_NOT_FOUND,
                        "Service availability not found with id " + availabilityId
                ));
    }

    @Override
    public List<ServiceAvailabilityDTO> getAllServiceAvb() {
        return serviceAvailabilityMapper.toListDto(serviceAvailabilityRepo.findAll());
    }


}
