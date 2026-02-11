package com.backend.apsor.mapper;

import com.backend.apsor.entities.ProviderMediaAsset;
import com.backend.apsor.payloads.dtos.ProviderMediaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class,uses = {
        MediaAssetMapper.class
})
public interface ProviderMediaAssetMapper {
    @Mapping(target = "providerMediaId", source = "id")
    @Mapping(target = "media", source = "media") // MediaAsset -> MediaAssetDTO via MediaAssetMapper
    ProviderMediaDTO toDto(ProviderMediaAsset entity);
}
