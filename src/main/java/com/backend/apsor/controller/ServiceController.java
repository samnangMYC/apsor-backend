package com.backend.apsor.controller;

import com.backend.apsor.payloads.requests.ServiceUpdateReq;
import com.backend.apsor.payloads.dtos.ServiceDTO;
import com.backend.apsor.payloads.requests.ServiceCreateReq;
import com.backend.apsor.service.ServiceService;
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
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
@Tag(
        name = "Services",
        description = "Role: CUSTOMER, PROVIDER. Service browsing + service management."
)
public class ServiceController {

    private final ServiceService serviceService;

    // support only provider to create
    @PostMapping
    public ResponseEntity<ServiceDTO> create(@AuthenticationPrincipal Jwt jwt,
                                             @Valid @RequestBody ServiceCreateReq req){
        log.debug("REST request to save Services : {}", req);
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceService.createNewService(jwt,req));
    }

    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAllServices(){
        log.debug("REST request to get all Services");
        return ResponseEntity.ok(serviceService.getAllService());
    }

    @PatchMapping
    public ResponseEntity<ServiceDTO> update(@AuthenticationPrincipal Jwt jwt,
                                             @Valid @RequestBody ServiceUpdateReq req){
        log.debug("REST request to update Services : {}", req);
        return ResponseEntity.ok(serviceService.updateService(jwt,req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@AuthenticationPrincipal Jwt jwt,@PathVariable Long id){
        log.debug("REST request to delete Services : {}", jwt);
        return ResponseEntity.ok(serviceService.deleteServiceById(jwt,id));
    }


}
