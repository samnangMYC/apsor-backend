package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.CustomerDTO;
import com.backend.apsor.payloads.requests.CustomerReq;
import com.backend.apsor.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Customer Management", description = "Endpoints for managing categories, including creation, retrieval, updates, and deletions.")
@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDTO> create(@AuthenticationPrincipal Jwt jwt,
                                              @Valid @RequestBody CustomerReq req) {
        log.debug("REST request to save Customer : {}", req);
        return ResponseEntity.status(201).body(customerService.createNewCustomer(jwt,req));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> findAll() {
        log.debug("REST request to get all Customers");
        return ResponseEntity.ok(customerService.getAllCustomer());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> findById(@PathVariable Long id) {
        log.debug("REST request to get Customer : {}", id);
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<CustomerDTO> findMeByJwt(@AuthenticationPrincipal Jwt jwt) {
        log.debug("REST request to get Customer by me : {}", jwt);
        return ResponseEntity.ok(customerService.getCustomerByJwt(jwt));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable Long id, @Valid @RequestBody CustomerReq req) {
        log.debug("REST request to update Customer : {}", req);
        return ResponseEntity.ok(customerService.updateCustomerById(id,req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        log.debug("REST request to delete Customer : {}", id);
        return ResponseEntity.status(204).body(customerService.deleteCustomerById(id));
    }


}
