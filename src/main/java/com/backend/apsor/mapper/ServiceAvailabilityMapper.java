package com.backend.apsor.mapper;

import com.backend.apsor.entities.ServiceAvailability;
import com.backend.apsor.payloads.dtos.ServiceAvailabilityDTO;
import com.backend.apsor.payloads.requests.ServiceAvailabilityReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface ServiceAvailabilityMapper {

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "timezone",ignore = true)
    @Mapping(target = "isDefault",ignore = true)
    @Mapping(target = "status",ignore = true)
    @Mapping(target = "service",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "updatedAt",ignore = true)
    ServiceAvailability toEntity(ServiceAvailabilityReq req);

    @Mapping(target = "serviceId",ignore = true)
    ServiceAvailabilityDTO toDto(ServiceAvailability availability);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "timezone",ignore = true)
    @Mapping(target = "isDefault",ignore = true)
    @Mapping(target = "status",ignore = true)
    @Mapping(target = "service",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "updatedAt",ignore = true)
    void update(ServiceAvailabilityReq req, @MappingTarget ServiceAvailability availability);

    List<ServiceAvailabilityDTO> toListDto(List<ServiceAvailability> availabilities);

}
