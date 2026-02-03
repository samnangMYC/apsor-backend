package com.backend.apsor.mapper;

import com.backend.apsor.entities.Users;
import com.backend.apsor.payloads.dtos.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config =  MapStructConfig.class)
public interface UserMapper {

    UserDTO toDTO(Users users);

    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "customer", ignore = true)
    Users toEntity(UserDTO userDTO);

    List<UserDTO> toListDTO(List<Users> usersList);
}
