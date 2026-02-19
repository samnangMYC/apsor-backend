package com.backend.apsor.mapper;

import com.backend.apsor.entities.Order;
import com.backend.apsor.payloads.dtos.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface OrderMapper {


    @Mapping(target = "serviceId", source = "service.id")
    @Mapping(target = "servicePriceId", source = "servicePrice.id")
    OrderDTO toDTO(Order order);

    List<OrderDTO> toDTOs(List<Order> orders);
}
