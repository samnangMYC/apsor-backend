package com.backend.apsor.service;

import com.backend.apsor.payloads.dtos.ServiceDTO;
import com.backend.apsor.payloads.requests.ServiceCreateReq;
import com.backend.apsor.payloads.requests.ServiceUpdateReq;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface ServiceService {
    ServiceDTO createNewService(Jwt jwt, @Valid ServiceCreateReq req);

    List<ServiceDTO> getAllService();

    ServiceDTO updateService(Jwt jwt, @Valid ServiceUpdateReq req);

    String deleteServiceById(Jwt jwt, Long id);
}
