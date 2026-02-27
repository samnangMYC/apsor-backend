package com.backend.apsor.mapper;

import com.backend.apsor.entities.Category;
import com.backend.apsor.entities.CategoryMedia;
import com.backend.apsor.entities.MediaAsset;
import com.backend.apsor.payloads.dtos.CategoryDTO;
import com.backend.apsor.payloads.requests.CategoryReq;
import org.mapstruct.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Mapper(config =  MapStructConfig.class)
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "subCategories",ignore = true)
    @Mapping(target = "services", ignore = true)
    @Mapping(target = "categoryMedia", ignore = true)
    @Mapping(target = "slug",ignore = true)
    Category toEntity(CategoryReq req);

    @Mapping(target = "imageUrl",source = "categoryMedia", qualifiedByName = "firstImageObjectKey")
    CategoryDTO toDto(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "subCategories",ignore = true)
    @Mapping(target = "services", ignore = true)
    @Mapping(target = "categoryMedia", ignore = true)
    @Mapping(target = "slug",ignore = true)
    void updateEntity(CategoryReq req,@MappingTarget Category category);

    @Mapping(target = "imageUrl",source = "categoryMedia", qualifiedByName = "firstImageObjectKey")
    List<CategoryDTO> toListDto(List<Category> categories);

    @Named("firstImageObjectKey")
    static String firstImageObjectKey(Collection<CategoryMedia> medias) {
        if (medias == null) return null;

        return medias.stream()
                .map(CategoryMedia::getMediaAsset)
                .filter(Objects::nonNull)
                .map(MediaAsset::getObjectKey)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
