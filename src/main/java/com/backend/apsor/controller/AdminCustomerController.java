package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.CustomerDTO;
import com.backend.apsor.payloads.requests.CustomerReq;
import com.backend.apsor.service.CustomerService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/customers")
@RequiredArgsConstructor
public class AdminCustomerController {

    private final CustomerService customerService;

    // admin
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> findAll() {
        log.debug("REST request to get all Customers");
        return ResponseEntity.ok(customerService.getAllCustomer());
    }

    // admin
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> findById(
            @Parameter(description = "Customer id", example = "5")
            @PathVariable Long id
    ) {
        log.debug("REST request to get Customer : {}", id);
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }
    // admin
    public ResponseEntity<CustomerDTO> update(
            @Parameter(description = "Customer id", example = "5")
            @PathVariable Long id,
            @Valid @RequestBody CustomerReq req
    ) {
        log.debug("REST request to update Customer : {}", req);
        return ResponseEntity.ok(customerService.updateCustomerById(id, req));
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<String> delete(
            @Parameter(description = "Customer id", example = "5")
            @PathVariable Long id
    ) {
        log.debug("REST request to delete Customer : {}", id);

        return ResponseEntity.ok(customerService.deleteCustomerById(id));
    }

}
