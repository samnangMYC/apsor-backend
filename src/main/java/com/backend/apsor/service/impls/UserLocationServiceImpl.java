package com.backend.apsor.service.impls;

import com.backend.apsor.entities.Location;
import com.backend.apsor.entities.UserLocation;
import com.backend.apsor.entities.Users;
import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.exceptions.ApiException;
import com.backend.apsor.mapper.UserLocationMapper;
import com.backend.apsor.payloads.dtos.UserLocationDTO;
import com.backend.apsor.payloads.requests.UserLocationReq;
import com.backend.apsor.mapper.LocationMapper;
import com.backend.apsor.repositories.LocationRepo;
import com.backend.apsor.repositories.UserLocationRepo;
import com.backend.apsor.service.UserLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserLocationServiceImpl implements UserLocationService {

    private final UserServiceImpl userServiceImpl;
    private final UserLocationRepo userLocationRepo;
    private final UserLocationMapper userLocationMapper;
    private final LocationRepo locationRepo;
    private final LocationMapper locationMapper;

    @Override
    public UserLocationDTO createNewLocation(Jwt jwt, UserLocationReq req) {
        Users users = userServiceImpl.loadUserByJwt(jwt);

        // 1) map & save Location first (shared table)
        Location location = locationMapper.toEntity(req);
        Location savedLocation = locationRepo.save(location);

        UserLocation userLocation = userLocationMapper.toEntity(req);
        userLocation.setUser(users);
        userLocation.setLocation(savedLocation);

        return userLocationMapper.toDto(userLocationRepo.save(userLocation));
    }

    @Override
    public List<UserLocationDTO> getAllUserLocation() {
        return userLocationMapper.toListDto(userLocationRepo.findAll());
    }

    @Override
    public UserLocationDTO getUserLocationById(Jwt jwt,Long userLocId) {
        return userLocationRepo.findById(userLocId)
                .map(userLocationMapper::toDto)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.LOCATION_NOT_FOUND,
                        "Location not found with id: ",
                        userLocId.toString()
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
    public UserLocationDTO updateUserLocationById(Jwt jwt,Long userLocId,UserLocationReq req) {
        Users user = userServiceImpl.loadUserByJwt(jwt);

        return userLocationRepo.findByIdAndUser_Id(userLocId,user.getId())
                .map(userLocation -> {
                    // 1) update link fields
                    userLocationMapper.updateEntity(req, userLocation);

                    // 2) update the referenced Location (important!)
                    Location loc = userLocation.getLocation();
                    if (loc == null) {
                        throw ApiException.conflict(
                                ApiErrorCode.LOCATION_NOT_FOUND,
                                "UserLocation has no location attached: %s",
                                userLocId.toString()
                        );
                    }

                    locationMapper.updateEntity(req, loc); // patch location fields from req
                    locationRepo.save(loc);

                    // 3) keep FK safe (optional but explicit)
                    userLocation.setUser(user);
                    userLocation.setLocation(loc);

                    return userLocationMapper.toDto(userLocationRepo.save(userLocation));
                }).orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.LOCATION_NOT_FOUND,
                        "Location not found with id: ",
                        userLocId.toString()
                ));
    }

    @Override
    public String deleteUserLocationById(Jwt jwt,Long userLocId) {
        Users user = userServiceImpl.loadUserByJwt(jwt);
        // enforce ownership and load in one query
        UserLocation userLocationDb = userLocationRepo.findByIdAndUser_Id(userLocId, user.getId())
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.LOCATION_NOT_FOUND,
                        "Location not found with id: %s",
                        userLocId.toString()
                ));

        Long locationId = userLocationDb.getLocation().getId();

        // 1) delete join row first (avoid FK issues)
        userLocationRepo.delete(userLocationDb);

        // 2) delete Location only if unused anywhere

            locationRepo.deleteById(locationId);

        return "Location deleted successfully";
    }
}
