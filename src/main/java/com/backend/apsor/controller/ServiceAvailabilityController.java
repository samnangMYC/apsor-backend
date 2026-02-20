package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.ServiceAvailabilityDTO;
import com.backend.apsor.payloads.requests.ServiceAvailabilityReq;
import com.backend.apsor.service.ServiceAvailabilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/service/availabilities")
@RequiredArgsConstructor
public class ServiceAvailabilityController {

    private final ServiceAvailabilityService serviceAvailabilityService;

    @PostMapping
    public ResponseEntity<ServiceAvailabilityDTO> create(@RequestBody ServiceAvailabilityReq req) {
        log.debug("REST request to save ServiceAvailability : {}", req);
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceAvailabilityService.createNewServiceAvb(req));
    }
    @GetMapping
    public ResponseEntity<List<ServiceAvailabilityDTO>> getAll() {
        log.debug("REST request to get all ServiceAvailability");
        return ResponseEntity.ok(serviceAvailabilityService.getAllServiceAvb());
    }

    // get availability by their id
    @GetMapping("/{id}")
    public ResponseEntity<ServiceAvailabilityDTO> getOne(@PathVariable("id") Long availabilityId) {
        log.debug("REST request to get one ServiceAvailability : {}", availabilityId);
        return ResponseEntity.ok(serviceAvailabilityService.getServiceAvailabilityByAvbId(availabilityId));

    }

    @PatchMapping("/{id}")
    public ResponseEntity<ServiceAvailabilityDTO> update(@PathVariable("id") Long availabilityId,
                                                         @RequestBody ServiceAvailabilityReq req) {
        log.debug("REST request to update ServiceAvailability : {}", availabilityId);
        return ResponseEntity.ok(serviceAvailabilityService.updateServiceAvailabilityByAvbId(availabilityId,req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long availabilityId) {
        log.debug("REST request to delete ServiceAvailability : {}", availabilityId);
        return ResponseEntity.ok(serviceAvailabilityService.deleteServiceAvailabilityByAvbId(availabilityId));
    }

}
