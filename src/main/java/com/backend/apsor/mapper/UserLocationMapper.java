package com.backend.apsor.mapper;

import com.backend.apsor.entities.UserLocation;
import com.backend.apsor.payloads.dtos.UserLocationDTO;
import com.backend.apsor.payloads.requests.UserLocationReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface UserLocationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserLocation toEntity(UserLocationReq locationReq);

    UserLocationDTO toDto(UserLocation userLocation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(UserLocationReq locationReq,@MappingTarget UserLocation userLocation);

    List<UserLocationDTO> toListDto(List<UserLocation> userLocations);

}
