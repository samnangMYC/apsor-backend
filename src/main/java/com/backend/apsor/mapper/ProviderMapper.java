package com.backend.apsor.mapper;

import com.backend.apsor.entities.Provider;
import com.backend.apsor.payloads.dtos.AdminProviderDTO;
import com.backend.apsor.payloads.dtos.ProviderDTO;
import com.backend.apsor.payloads.requests.AdminProviderReq;
import com.backend.apsor.payloads.requests.ProviderReq;
import org.mapstruct.*;

import java.util.List;

@Mapper(config =  MapStructConfig.class)
public interface ProviderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "mediaAssets", ignore = true)
    @Mapping(target = "isAvailable", ignore = true)
    @Mapping(target = "status",ignore = true)
    Provider toEntity(ProviderReq req);

    ProviderDTO toDTO(Provider provider);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "mediaAssets", ignore = true)
    @Mapping(target = "isAvailable", ignore = true)
    @Mapping(target = "status",ignore = true)
    void update(@MappingTarget Provider entity, ProviderReq req);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "mediaAssets", ignore = true)
    @Mapping(target = "isAvailable", ignore = true)
    @Mapping(target = "status",ignore = true)
    Provider toEntityFromAdmin(AdminProviderReq req);

    AdminProviderDTO toAdminDto(Provider provider);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "mediaAssets", ignore = true)
    @Mapping(target = "isAvailable", ignore = true)
    @Mapping(target = "status",ignore = true)
    void updateAdminProvider(AdminProviderReq req, @MappingTarget Provider provider);

    List<AdminProviderDTO> toAdminListDto(List<Provider> providers);




}
