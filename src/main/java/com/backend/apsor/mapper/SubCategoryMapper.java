package com.backend.apsor.mapper;

import com.backend.apsor.entities.SubCategory;
import com.backend.apsor.payloads.dtos.SubCategoryDTO;
import com.backend.apsor.payloads.requests.SubCategoryReq;
import com.backend.apsor.payloads.requests.SubCategoryUpdateReq;
import org.mapstruct.*;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface SubCategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "services", ignore = true)
    SubCategory toEntity(SubCategoryReq req);

    @Mapping(target = "categoryId", source = "category.id")
    SubCategoryDTO toDto(SubCategory category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "services", ignore = true)
    void updateEntity(SubCategoryUpdateReq req, @MappingTarget SubCategory category);

    List<SubCategoryDTO> toListDto(List<SubCategory> categories);
}
