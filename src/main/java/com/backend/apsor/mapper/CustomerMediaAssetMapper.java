package com.backend.apsor.mapper;

import com.backend.apsor.entities.CustomerMediaAsset;
import com.backend.apsor.payloads.dtos.CustomerMediaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class,uses = {MediaAssetMapper.class})
public interface CustomerMediaAssetMapper {
    @Mapping(target = "customerMediaId", source = "id")
    @Mapping(target = "media", source = "media")
    CustomerMediaDTO toDto(CustomerMediaAsset entity);
}
