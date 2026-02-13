package com.backend.apsor.service;

import com.backend.apsor.payloads.dtos.UserLocationDTO;
import com.backend.apsor.payloads.requests.UserLocationReq;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface UserLocationService {
    UserLocationDTO createNewLocation(Jwt jwt, @Valid UserLocationReq req);

    List<UserLocationDTO> getAllUserLocation();

    UserLocationDTO getUserLocationById(Jwt jwt,Long id);

    UserLocationDTO getUserLocationByJwt(Jwt jwt);

    UserLocationDTO updateUserLocationById(Jwt jwt,Long userLocId,UserLocationReq req);

    String deleteUserLocationById(Jwt jwt,Long id);
}
