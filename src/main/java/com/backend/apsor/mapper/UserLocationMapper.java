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
    @Mapping(target = "location", ignore = true)
    UserLocation toEntity(UserLocationReq locationReq);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "locationId", source = "location.id")
    @Mapping(target = "line1", source = "location.line1")
    @Mapping(target = "line2", source = "location.line2")
    @Mapping(target = "district", source = "location.district")
    @Mapping(target = "city", source = "location.city")
    @Mapping(target = "province", source = "location.province")
    @Mapping(target = "postalCode", source = "location.postalCode")
    @Mapping(target = "latitude", source = "location.latitude")
    @Mapping(target = "longitude", source = "location.longitude")
    UserLocationDTO toDto(UserLocation userLocation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "location", ignore = true)
    void updateEntity(UserLocationReq locationReq,@MappingTarget UserLocation userLocation);

    List<UserLocationDTO> toListDto(List<UserLocation> userLocations);

}
