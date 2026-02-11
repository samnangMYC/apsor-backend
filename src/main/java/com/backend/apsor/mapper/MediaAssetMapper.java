package com.backend.apsor.mapper;

import com.backend.apsor.entities.MediaAsset;
import com.backend.apsor.payloads.dtos.MediaAssetDTO;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)

public interface MediaAssetMapper {
    MediaAssetDTO toDto(MediaAsset entity);
}
