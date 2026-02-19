package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.ServicePriceDTO;
import com.backend.apsor.payloads.requests.ServiceCreatePriceReq;
import com.backend.apsor.payloads.requests.ServiceUpdatePriceReq;
import com.backend.apsor.service.ServicePriceService;
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
@RequestMapping("/api/v1/services/prices")
@RequiredArgsConstructor
public class ServicePriceController {

    private final ServicePriceService servicePriceService;

    @PostMapping("/{id}")
    public ResponseEntity<ServicePriceDTO> create(@AuthenticationPrincipal Jwt jwt,
                                                           @PathVariable("id") Long serviceId,
                                                           @RequestBody ServiceCreatePriceReq req) {
        log.debug("REST request to save ServicePrice : {}", req);
        return ResponseEntity.status(HttpStatus.CREATED).body(servicePriceService.createNewServicePrice(jwt,serviceId,req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ServicePriceDTO>> getAll(@AuthenticationPrincipal Jwt jwt,
                                                        @PathVariable("id") Long serviceId) {
        log.debug("REST request to get ServicePrice : {}", serviceId);
        return ResponseEntity.ok(servicePriceService.getAllServicePriceByServiceId(jwt,serviceId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ServicePriceDTO> update(@AuthenticationPrincipal Jwt jwt,
                                                        @PathVariable("id") Long serviceId,
                                                        @RequestBody ServiceUpdatePriceReq req) {
        log.debug("REST request to update ServicePrice : {}", req);
        return ResponseEntity.ok(servicePriceService.updateServicePrice(jwt,serviceId,req));
    }

    @DeleteMapping("/{id}/delete/{servicePriceId}")
    public ResponseEntity<String> delete(@AuthenticationPrincipal Jwt jwt,
                                         @PathVariable("id") Long serviceId,
                                         @PathVariable Long servicePriceId) {
        log.debug("REST request to delete ServicePrice : {}", serviceId);
        return ResponseEntity.ok(servicePriceService.deleteServicePrice(jwt,serviceId,servicePriceId));
    }

}
