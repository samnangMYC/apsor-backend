package com.backend.apsor.mapper;

import com.backend.apsor.entities.ServiceLocation;
import com.backend.apsor.payloads.dtos.ServiceLocationDTO;
import com.backend.apsor.payloads.requests.ServiceLocationReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface ServiceLocationMapper {

    @Mapping(target = "line1", source = "location.line1")
    @Mapping(target = "line2", source = "location.line2")
    @Mapping(target = "district", source = "location.district")
    @Mapping(target = "city", source = "location.city")
    @Mapping(target = "province", source = "location.province")
    @Mapping(target = "postalCode", source = "location.postalCode")
    @Mapping(target = "latitude", source = "location.latitude")
    @Mapping(target = "longitude", source = "location.longitude")
    ServiceLocationDTO toDto(ServiceLocation serviceLocation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "service", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void update(ServiceLocationReq req, @MappingTarget ServiceLocation serviceLocation);

    List<ServiceLocationDTO> toListDto(List<ServiceLocation> serviceLocations);


}
