package com.backend.apsor.mapper;

import com.backend.apsor.entities.ServiceMedia;
import com.backend.apsor.payloads.dtos.ServiceMediaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(config = MapStructConfig.class, uses = {MediaAssetMapper.class})
public interface ServiceMediaMapper {

    @Mapping(target = "serviceMediaId", source = "id")
    @Mapping(target = "purpose", source = "mediaPurpose")     // entity field name
    @Mapping(target = "media", source = "mediaAsset")         // entity field name
    @Mapping(target = "sortOrder", source = "sortOrder")
    @Mapping(target = "status", source = "status")
    ServiceMediaDTO toDto(ServiceMedia serviceMedia);

    List<ServiceMediaDTO> toListDto(List<ServiceMedia> serviceMedia);
}
