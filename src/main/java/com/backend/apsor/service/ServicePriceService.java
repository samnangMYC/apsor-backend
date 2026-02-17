package com.backend.apsor.service;

import com.backend.apsor.payloads.dtos.ServicePriceDTO;
import com.backend.apsor.payloads.requests.ServiceCreatePriceReq;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface ServicePriceService {
    ServicePriceDTO createNewServicePrice(Jwt jwt,Long serviceId, ServiceCreatePriceReq req);

    List<ServicePriceDTO> getAllServicePriceByServiceId(Long serviceId);
}
