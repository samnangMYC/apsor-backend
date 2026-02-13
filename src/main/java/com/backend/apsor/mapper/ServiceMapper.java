package com.backend.apsor.mapper;

import com.backend.apsor.entities.Services;
import com.backend.apsor.payloads.dtos.ServiceDTO;
import com.backend.apsor.payloads.requests.ServiceCreateReq;
import com.backend.apsor.payloads.requests.ServiceUpdateReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config =  MapStructConfig.class)
public interface ServiceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "ratingAvg", ignore = true)
    @Mapping(target = "ratingCount", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "suspendedAt", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "subcategory", ignore = true)
    @Mapping(target = "locations", ignore = true)
    @Mapping(target = "medias", ignore = true)
    @Mapping(target = "prices", ignore = true)
    Services toEntity(ServiceCreateReq req);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "ratingAvg", ignore = true)
    @Mapping(target = "ratingCount", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "suspendedAt", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "subcategory", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "locations", ignore = true)
    @Mapping(target = "medias", ignore = true)
    @Mapping(target = "prices", ignore = true)
    Services toEntity(ServiceUpdateReq req);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "subCategoryId",source = "subcategory.id")
    @Mapping(target = "providerId", source = "provider.id")
    ServiceDTO toDTO(Services services);

    void update(Services services, @MappingTarget Services servicesUpdate);

    List<ServiceDTO> toDTOs(List<Services> services);

}
