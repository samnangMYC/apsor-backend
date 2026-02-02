package com.backend.apsor.mapper;

import com.backend.apsor.entities.Users;
import com.backend.apsor.payloads.dtos.UserDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config =  MapStructConfig.class)
public interface UserMapper {

    UserDTO toDTO(Users users);

    Users toEntity(UserDTO userDTO);

    List<UserDTO> toListDTO(List<Users> usersList);
}
