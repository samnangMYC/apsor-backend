package com.backend.apsor.service;

import com.backend.apsor.entities.Users;
import com.backend.apsor.enums.UserType;
import com.backend.apsor.payloads.dtos.UserDTO;
import com.backend.apsor.payloads.requests.*;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface UserService {

    UserDTO signUpCustomer(@Valid SignUpReq req);

    UserDTO signUpProvider(@Valid SignUpReq req);

    UserDTO getMe(Jwt jwt);

    UserDTO updateMe(Jwt jwt, @Valid UpdateMeReq req);

    UserDTO createUserByAdmin(@Valid CreateUserByAdminReq req);

    UserDTO updateUserByAdmin(Long id, UpdateUserByAdminReq req);

    String softDeleteUserByAdmin(Long id);

    String  hardDeleteUserByAdmin(Long id);

    UserDTO getUserByIdFromAdmin(Long id);

    List<UserDTO> getAllUserFromAdmin();

    Users loadUserByJwt(Jwt jwt);

    UserDTO updateUserTypeByAdmin(Long userId, UserTypeReq type);
}
