package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.ServicePriceDTO;
import com.backend.apsor.payloads.requests.ServiceCreatePriceReq;
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

    @PostMapping("/{serviceId}")
    public ResponseEntity<ServicePriceDTO> create(@AuthenticationPrincipal Jwt jwt,
                                                           @PathVariable Long serviceId,
                                                           @RequestBody ServiceCreatePriceReq req) {
        log.debug("REST request to save ServicePrice : {}", req);
        return ResponseEntity.status(HttpStatus.CREATED).body(servicePriceService.createNewServicePrice(jwt,serviceId,req));
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<List<ServicePriceDTO>> getAll(@PathVariable Long serviceId) {
        log.debug("REST request to get ServicePrice : {}", serviceId);
        return ResponseEntity.ok(servicePriceService.getAllServicePriceByServiceId(serviceId));
    }

}
