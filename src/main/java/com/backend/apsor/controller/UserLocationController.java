package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.UserLocationDTO;
import com.backend.apsor.payloads.requests.UserLocationReq;
import com.backend.apsor.service.UserLocationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user/locations")
@RequiredArgsConstructor
@Tag(
        name = "Locations",
        description = "Role: CUSTOMER, PROVIDER. Manage user and provider saved locations/addresses."
)
@SecurityRequirement(name = "bearerAuth")
public class UserLocationController {

    private final UserLocationService userLocationService;

    // this endpoint not support admin yet
    @PostMapping
    public ResponseEntity<UserLocationDTO> create(@AuthenticationPrincipal Jwt jwt,
                                                  @Valid @RequestBody UserLocationReq req) {
        log.debug("REST request to save UserLocation : {}", req);
        return ResponseEntity.status(HttpStatus.CREATED).body(userLocationService.createNewLocation(jwt,req));

    }

    @GetMapping
    public ResponseEntity<List<UserLocationDTO>> findAll(){
        log.debug("REST request to get all UserLocations");
        return ResponseEntity.ok(userLocationService.getAllUserLocation());
    }

    @GetMapping("/me")
    public ResponseEntity<UserLocationDTO> findByMe(@AuthenticationPrincipal Jwt jwt){
        log.debug("REST request to get UserLocation by jwt: {}", jwt);
        return ResponseEntity.ok(userLocationService.getUserLocationByJwt(jwt));
    }

    @GetMapping("/{userLocId}")
    public ResponseEntity<UserLocationDTO> findById(@AuthenticationPrincipal Jwt jwt,
                                                    @PathVariable Long userLocId){
        log.debug("REST request to get UserLocation : {}", userLocId);
        return ResponseEntity.ok(userLocationService.getUserLocationById(jwt,userLocId));
    }

    @PatchMapping("/{userLocId}")
    public ResponseEntity<UserLocationDTO> update(@AuthenticationPrincipal Jwt jwt,
                                                  @PathVariable Long userLocId,
                                                  @RequestBody UserLocationReq req){
        log.debug("REST request to update UserLocation : {}", req);
        return ResponseEntity.ok(userLocationService.updateUserLocationById(jwt,userLocId,req));
    }

    @DeleteMapping("/{userLocId}")
    public ResponseEntity<String> delete(@AuthenticationPrincipal Jwt jwt,
                                         @PathVariable Long userLocId){
        log.debug("REST request to delete UserLocation : {}", userLocId);
        return ResponseEntity.status(204).body(userLocationService.deleteUserLocationById(jwt,userLocId));
    }
}
