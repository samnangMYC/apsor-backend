package com.backend.apsor.service.impls;

import com.backend.apsor.entities.Location;
import com.backend.apsor.entities.ServiceLocation;
import com.backend.apsor.entities.Services;
import com.backend.apsor.entities.Users;
import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.exceptions.ApiException;
import com.backend.apsor.mapper.LocationMapper;
import com.backend.apsor.mapper.ServiceLocationMapper;
import com.backend.apsor.payloads.dtos.ServiceLocationDTO;
import com.backend.apsor.payloads.requests.ServiceLocationReq;
import com.backend.apsor.repositories.LocationRepo;
import com.backend.apsor.repositories.ServiceLocationRepo;
import com.backend.apsor.repositories.ServiceRepo;
import com.backend.apsor.service.ServiceLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceLocationImpl implements ServiceLocationService {
    private final ServiceRepo serviceRepo;
    private final LocationRepo locationRepo;
    private final ServiceLocationRepo serviceLocationRepo;
    private final LocationMapper locationMapper;
    private final ServiceLocationMapper serviceLocationMapper;
    private final UserServiceImpl userServiceImpl;

    @Override
    public ServiceLocationDTO createNewServiceLoc(ServiceLocationReq req) {
        Services services = serviceRepo.findById(req.getServiceId())
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SERVICE_NOT_FOUND,
                        "Service not found with id: " + req.getServiceId()
                ));

        Location location = locationMapper.toEntity(req);

        locationRepo.save(location);

        ServiceLocation serviceLocation = ServiceLocation.builder()
                .isDefault(false)
                .service(services)
                .location(location).build();

        serviceLocationRepo.save(serviceLocation);

        return serviceLocationMapper.toDto(serviceLocation);
    }

    @Override
    public ServiceLocationDTO getServiceById(Long serviceLocId) {
        return serviceLocationRepo.findById(serviceLocId)
                .map(serviceLocationMapper::toDto)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SERVICE_LOCATION_NOT_FOUND,
                        "Service not found with id: " + serviceLocId
                ));
    }

    @Override
    public ServiceLocationDTO updateServiceLocById(Long serviceLocId, ServiceLocationReq req) {
        return serviceLocationRepo.findById(serviceLocId)
                .map(serviceLocation -> {
                      serviceLocationMapper.update(req,serviceLocation);
                      serviceLocationRepo.save(serviceLocation);
                      return serviceLocationMapper.toDto(serviceLocation);
                }).orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SERVICE_LOCATION_NOT_FOUND,
                        "Service not found with id: " + serviceLocId
                ));
    }

    @Override
    public String deleteServiceLocById(Long serviceLocId) {
        return serviceLocationRepo.findById(serviceLocId)
                .map(serviceLocation -> {
                    serviceLocationRepo.delete(serviceLocation);
                    return "Successfully deleted service location";
                }).orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SERVICE_LOCATION_NOT_FOUND,
                        "Service not found with id: " + serviceLocId
                ));
    }

    @Override
    public List<ServiceLocationDTO> getAllServiceLoc() {
        return serviceLocationMapper.toListDto(serviceLocationRepo.findAll());
    }
}
