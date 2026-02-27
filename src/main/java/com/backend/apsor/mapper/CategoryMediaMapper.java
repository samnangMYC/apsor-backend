package com.backend.apsor.mapper;

import com.backend.apsor.entities.CategoryMedia;
import com.backend.apsor.payloads.dtos.CategoryMediaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config =  MapStructConfig.class)
public interface CategoryMediaMapper {

    @Mapping(target = "categoryMediaId", source = "id")
    @Mapping(target = "media", source = "mediaAsset")
    CategoryMediaDTO toDto(CategoryMedia categoryMedia);

    List<CategoryMediaDTO> toListDto(List<CategoryMedia> categoryMedia);
}
