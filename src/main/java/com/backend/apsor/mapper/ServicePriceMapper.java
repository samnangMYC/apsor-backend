package com.backend.apsor.mapper;

import com.backend.apsor.entities.ServicePrice;
import com.backend.apsor.payloads.dtos.ServicePriceDTO;
import com.backend.apsor.payloads.requests.ServiceCreatePriceReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config =  MapStructConfig.class)
public interface ServicePriceMapper {

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "status",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "updateAt",ignore = true)
    ServicePrice toEntity(ServiceCreatePriceReq req);

    ServicePriceDTO toDTO(ServicePrice servicePrice);

    void update(ServiceCreatePriceReq req, @MappingTarget ServicePrice servicePrice);

    List<ServicePriceDTO> toListDTO(List<ServicePrice> servicePrices);

}
