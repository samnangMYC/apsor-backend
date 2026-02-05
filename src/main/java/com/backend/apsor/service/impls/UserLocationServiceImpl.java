package com.backend.apsor.service.impls;

import com.backend.apsor.entities.UserLocation;
import com.backend.apsor.entities.Users;
import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.exceptions.ApiException;
import com.backend.apsor.mapper.UserLocationMapper;
import com.backend.apsor.payloads.dtos.UserLocationDTO;
import com.backend.apsor.payloads.requests.UserLocationReq;
import com.backend.apsor.repositories.UserLocationRepo;
import com.backend.apsor.service.UserLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLocationServiceImpl implements UserLocationService {

    private final UserServiceImpl userServiceImpl;
    private final UserLocationRepo userLocationRepo;
    private final UserLocationMapper userLocationMapper;

    @Override
    public UserLocationDTO createNewLocation(Jwt jwt, UserLocationReq req) {
        Users users = userServiceImpl.loadUserByJwt(jwt);
        UserLocation userLocation = userLocationMapper.toEntity(req);
        userLocation.setUser(users);

        return userLocationMapper.toDto(userLocationRepo.save(userLocation));
    }

    @Override
    public List<UserLocationDTO> getAllUserLocation() {
        return userLocationMapper.toListDto(userLocationRepo.findAll());
    }

    @Override
    public UserLocationDTO getUserLocationById(Long id) {
        return userLocationRepo.findById(id)
                .map(userLocationMapper::toDto)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.LOCATION_NOT_FOUND,
                        "Location not found with id: ",
                        id.toString()
                ));
    }

    @Override
    public UserLocationDTO getUserLocationByJwt(Jwt jwt) {
        Users users = userServiceImpl.loadUserByJwt(jwt);

        return userLocationRepo.findByUser_Id(users.getId())
                .map(userLocationMapper::toDto)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.USER_NOT_FOUND,
                        "User not found with id: ",
                        users.getId().toString()
                ));
    }

    @Override
    public UserLocationDTO updateUserLocationById(Long id,UserLocationReq req) {
        return userLocationRepo.findById(id)
                .map(userLocation -> {
                    UserLocation location = userLocationRepo.save(userLocationMapper.toEntity(req));
                    return userLocationMapper.toDto(location);
                }).orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.LOCATION_NOT_FOUND,
                        "Location not found with id: ",
                        id.toString()
                ));
    }

    @Override
    public String deleteUserLocationById(Long id) {
        return userLocationRepo.findById(id)
                .map(userLocation -> {
                    userLocationRepo.delete(userLocation);
                    return "Location deleted successfully";
                }).orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.LOCATION_NOT_FOUND,
                        "Location not found with id: ",
                        id.toString()
                ));
    }
}
