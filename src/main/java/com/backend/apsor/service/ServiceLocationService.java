package com.backend.apsor.service;

import com.backend.apsor.payloads.dtos.ServiceLocationDTO;
import com.backend.apsor.payloads.requests.ServiceLocationReq;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface ServiceLocationService {
    ServiceLocationDTO createNewServiceLoc(ServiceLocationReq req);

    ServiceLocationDTO getServiceById(Long serviceLocId);

    ServiceLocationDTO updateServiceLocById(Long serviceLocId, ServiceLocationReq req);

    String deleteServiceLocById(Long serviceLocId);

    List<ServiceLocationDTO> getAllServiceLoc();
}
