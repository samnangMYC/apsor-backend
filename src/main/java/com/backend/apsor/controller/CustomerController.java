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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@Tag(
        name = "Customer Management",
        description = "Endpoints for managing customer profiles (create, view, update, delete)."
)
@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @Operation(
            summary = "Create customer profile",
            description = "Creates a customer profile for the authenticated user (shared primary key with user)."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "409", description = "Customer already exists", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CustomerDTO> create(
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CustomerReq req
    ) {
        log.debug("REST request to save Customer : {}", req);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createNewCustomer(jwt, req));
    }

    @Operation(
            summary = "Get all customers",
            description = "Returns all customer profiles."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> findAll() {
        log.debug("REST request to get all Customers");
        return ResponseEntity.ok(customerService.getAllCustomer());
    }

    @Operation(
            summary = "Get customer by id",
            description = "Returns a single customer profile by id."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> findById(
            @Parameter(description = "Customer id", example = "5")
            @PathVariable Long id
    ) {
        log.debug("REST request to get Customer : {}", id);
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @Operation(
            summary = "Get my customer profile",
            description = "Returns the customer profile of the authenticated user."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<CustomerDTO> findMeByJwt(
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt
    ) {
        log.debug("REST request to get Customer by me");
        return ResponseEntity.ok(customerService.getCustomerByJwt(jwt));
    }

    @Operation(
            summary = "Update customer profile",
            description = "Partially updates a customer profile by id."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated",
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(
            @Parameter(description = "Customer id", example = "5")
            @PathVariable Long id,
            @Valid @RequestBody CustomerReq req
    ) {
        log.debug("REST request to update Customer : {}", req);
        return ResponseEntity.ok(customerService.updateCustomerById(id, req));
    }

    @Operation(
            summary = "Delete customer profile",
            description = "Deletes a customer profile by id."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Customer id", example = "5")
            @PathVariable Long id
    ) {
        log.debug("REST request to delete Customer : {}", id);
        customerService.deleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }
}
