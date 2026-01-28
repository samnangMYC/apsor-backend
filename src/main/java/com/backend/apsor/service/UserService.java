package com.backend.apsor.service;

import com.backend.apsor.enums.UserType;
import com.backend.apsor.payloads.dtos.UserDTO;
import com.backend.apsor.payloads.requests.AdminUpdateUserReq;
import com.backend.apsor.payloads.requests.SignUpReq;
import com.backend.apsor.payloads.requests.UpdateMeReq;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserService {

    UserDTO signUpCustomer(SignUpReq req);

    UserDTO signUpProvider(SignUpReq req);

    UserDTO createAdmin(SignUpReq req);

    UserDTO getMe(Jwt jwt);

    UserDTO updateMe(Jwt jwt, @Valid UpdateMeReq req);

    UserDTO adminUpdate(Long id, @Valid AdminUpdateUserReq req);

    void deleteUser(Long id);
}
