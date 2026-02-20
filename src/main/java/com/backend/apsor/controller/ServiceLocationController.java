package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.ServiceLocationDTO;
import com.backend.apsor.payloads.requests.ServiceLocationReq;
import com.backend.apsor.service.ServiceLocationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/service/locations")
@RequiredArgsConstructor
@Tag(
        name = "Service Locations",
        description = "Role: PROVIDER. Manage user and provider saved locations."
)
@SecurityRequirement(name = "bearerAuth")
public class ServiceLocationController {

    private final ServiceLocationService serviceLocationService;

    @PostMapping
    public ResponseEntity<ServiceLocationDTO> create(@RequestBody ServiceLocationReq req) {
        log.debug("REST request to save ServiceLocation : {}", req);
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceLocationService.createNewServiceLoc(req));

    }

    @GetMapping
    public ResponseEntity<List<ServiceLocationDTO>> getAll() {
        log.debug("REST request to get all ServiceLocations");
        return ResponseEntity.ok(serviceLocationService.getAllServiceLoc());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceLocationDTO> getOne(@PathVariable("id") Long serviceLocId) {
        log.debug("REST request to get ServiceLocation : {}", serviceLocId);
        return ResponseEntity.ok(serviceLocationService.getServiceById(serviceLocId));

    }

    @PatchMapping("/{id}")
    public ResponseEntity<ServiceLocationDTO> update(@PathVariable("id") Long serviceLocId,
                                                     @RequestBody ServiceLocationReq req) {
        log.debug("REST request to update ServiceLocation : {}", req);
        return ResponseEntity.ok(serviceLocationService.updateServiceLocById(serviceLocId,req));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long serviceLocId) {
       log.debug("REST request to delete ServiceLocation : {}", serviceLocId);
        return ResponseEntity.ok( serviceLocationService.deleteServiceLocById(serviceLocId));
    }

}
