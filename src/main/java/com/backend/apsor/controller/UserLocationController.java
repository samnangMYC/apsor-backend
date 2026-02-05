package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.UserLocationDTO;
import com.backend.apsor.payloads.requests.UserLocationReq;
import com.backend.apsor.service.UserLocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
public class UserLocationController {

    private final UserLocationService userLocationService;

    @PostMapping
    public ResponseEntity<UserLocationDTO> create(@AuthenticationPrincipal Jwt jwt,
                                                  @Valid @RequestBody UserLocationReq req) {
        log.debug("REST request to save UserLocation : {}", req);
        return ResponseEntity.status(201).body(userLocationService.createNewLocation(jwt,req));

    }

    @GetMapping
    public ResponseEntity<List<UserLocationDTO>> findAll(){
        log.debug("REST request to get all UserLocations");
        return ResponseEntity.ok(userLocationService.getAllUserLocation());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserLocationDTO> findById(@PathVariable("userId") Long id){
        log.debug("REST request to get UserLocation : {}", id);
        return ResponseEntity.ok(userLocationService.getUserLocationById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<UserLocationDTO> findByMe(@AuthenticationPrincipal Jwt jwt){
        log.debug("REST request to get UserLocation by jwt: {}", jwt);
        return ResponseEntity.ok(userLocationService.getUserLocationByJwt(jwt));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserLocationDTO> update(@PathVariable("userId") Long id,
                                                  @RequestBody UserLocationReq req){
        log.debug("REST request to update UserLocation : {}", req);
        return ResponseEntity.ok(userLocationService.updateUserLocationById(id,req));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable("userId") Long id){
        log.debug("REST request to delete UserLocation : {}", id);
        return ResponseEntity.status(204).body(userLocationService.deleteUserLocationById(id));
    }
}
