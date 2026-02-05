package com.backend.apsor.mapper;

import com.backend.apsor.entities.Category;
import com.backend.apsor.payloads.dtos.CategoryDTO;
import com.backend.apsor.payloads.requests.CategoryReq;
import org.mapstruct.*;

import java.util.List;

@Mapper(config =  MapStructConfig.class)
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "subCategories",ignore = true)
    Category toEntity(CategoryReq req);

    CategoryDTO toDto(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "subCategories",ignore = true)
    void updateEntity(CategoryReq req,@MappingTarget Category category);

    List<CategoryDTO> toListDto(List<Category> categories);
}
