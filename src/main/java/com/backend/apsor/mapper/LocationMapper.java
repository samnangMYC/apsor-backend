package com.backend.apsor.mapper;

import com.backend.apsor.entities.Location;
import com.backend.apsor.payloads.requests.UserLocationReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface LocationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "serviceLocations", ignore = true)
    @Mapping(target = "userLocations", ignore = true)
    Location toEntity(UserLocationReq req);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "serviceLocations", ignore = true)
    @Mapping(target = "userLocations", ignore = true)
    void updateEntity(UserLocationReq locationReq,@MappingTarget Location userLocation);
}
