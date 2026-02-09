package com.backend.apsor.mapper;

import com.backend.apsor.entities.Customer;
import com.backend.apsor.payloads.dtos.CustomerDTO;
import com.backend.apsor.payloads.requests.CustomerReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "onboardingCompleted", ignore = true)
    Customer toEntity(CustomerReq customerReq);

    @Mapping(target = "userId", source = "id")
    CustomerDTO toDto(Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "onboardingCompleted", ignore = true)
    void updateEntity(CustomerReq customerReq, @MappingTarget Customer customer);

    List<CustomerDTO> toListDto(List<Customer> customers);
}
