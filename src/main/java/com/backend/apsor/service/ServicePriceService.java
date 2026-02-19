package com.backend.apsor.service;

import com.backend.apsor.payloads.dtos.ServicePriceDTO;
import com.backend.apsor.payloads.requests.ServiceCreatePriceReq;
import com.backend.apsor.payloads.requests.ServiceUpdatePriceReq;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface ServicePriceService {
    ServicePriceDTO createNewServicePrice(Jwt jwt,Long serviceId, ServiceCreatePriceReq req);

    List<ServicePriceDTO> getAllServicePriceByServiceId(Jwt jwt,Long serviceId);

    ServicePriceDTO updateServicePrice(Jwt jwt, Long serviceId, ServiceUpdatePriceReq req);

    String deleteServicePrice(Jwt jwt, Long serviceId,Long servicePriceId);
}
