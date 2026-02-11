package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.CustomerDTO;
import com.backend.apsor.payloads.dtos.CustomerMediaDTO;
import com.backend.apsor.payloads.dtos.ProviderMediaDTO;
import com.backend.apsor.payloads.requests.CustomerReq;
import com.backend.apsor.service.CustomerService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/{customerId}/avartar")
    public ResponseEntity<CustomerMediaDTO> avatar(@PathVariable Long customerId,
                                                   @RequestPart MultipartFile file) {
        log.debug("REST request to avatar for customer : {}", customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                customerService.uploadNewAvatarFromAdmin(customerId,file)
        );
    }

    @GetMapping("/{customerId}/avartar")
    public ResponseEntity<CustomerMediaDTO> getAvatar(@PathVariable Long customerId) {
        log.debug("REST request to get Customer avatar: {}", customerId);
        return ResponseEntity.ok(customerService.getAvatarByIdFromAdmin(customerId));
    }

    @PatchMapping("/{customerId}/avartar")
    public ResponseEntity<CustomerMediaDTO> updateAvatar(@PathVariable Long customerId,
                                                         @RequestPart MultipartFile file){
        log.debug("REST request to update avatar for customer : {}", customerId);
        return ResponseEntity.ok(customerService.updateByIdFromAdmin(customerId,file));
    }

    @DeleteMapping("{customerId}/avatar/{mediaId}")
    public ResponseEntity<String> deleteAvatar(@PathVariable Long customerId,@PathVariable Long mediaId) {
        log.debug("Deleting provider resource : {} ", mediaId);
        return ResponseEntity.ok(customerService.deleteAvatarByMediaIdFromAdmin(customerId,mediaId));
    }


}
