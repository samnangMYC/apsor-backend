package com.backend.apsor.service;

import com.backend.apsor.payloads.dtos.ServiceAvailabilityDTO;
import com.backend.apsor.payloads.requests.ServiceAvailabilityReq;

import java.util.List;


public interface ServiceAvailabilityService {
    ServiceAvailabilityDTO createNewServiceAvb(ServiceAvailabilityReq req);

    ServiceAvailabilityDTO getServiceAvailabilityByAvbId(Long availabilityId);

    ServiceAvailabilityDTO updateServiceAvailabilityByAvbId(Long availabilityId,ServiceAvailabilityReq req);

    String deleteServiceAvailabilityByAvbId(Long availabilityId);

    List<ServiceAvailabilityDTO> getAllServiceAvb();
}
