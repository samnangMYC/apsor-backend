package com.backend.apsor.service;

import com.backend.apsor.payloads.dtos.UserDTO;
import com.backend.apsor.payloads.requests.CreateUserByAdminReq;
import com.backend.apsor.payloads.requests.SignUpReq;
import com.backend.apsor.payloads.requests.UpdateMeReq;
import com.backend.apsor.payloads.requests.UpdateUserByAdminReq;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserService {

    UserDTO signUpCustomer(@Valid SignUpReq req);

    UserDTO signUpProvider(@Valid SignUpReq req);

    UserDTO getMe(Jwt jwt);

    UserDTO updateMe(Jwt jwt, @Valid UpdateMeReq req);

    void deleteUser(Long id);

    UserDTO createUserByAdmin(@Valid CreateUserByAdminReq req);

    UserDTO updateUserByAdmin(Long id, UpdateUserByAdminReq req);
}
