package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.CustomerDTO;
import com.backend.apsor.payloads.dtos.MediaAssetDTO;
import com.backend.apsor.payloads.dtos.ProviderMediaDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(
        name = "Customers",
        description = "Role: CUSTOMER. Customer profile and customer operations."
)
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDTO> create(
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CustomerReq req
    ) {
        log.debug("REST request to save Customer : {}", req);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createNewCustomer(jwt, req));
    }

    @GetMapping
    public ResponseEntity<CustomerDTO> findMe(
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt
    ) {
        log.debug("REST request to get Customer by me");
        return ResponseEntity.ok(customerService.getCustomerByJwt(jwt));
    }

    @PatchMapping
    public ResponseEntity<CustomerDTO> update(@AuthenticationPrincipal Jwt jwt,
                                              @Valid @RequestBody CustomerReq req){
        log.debug("REST request to update Customer : {}", req);
        return ResponseEntity.ok(customerService.updateCustomerByJwt(jwt,req));

    }

    @PostMapping("/avatar")
    public ResponseEntity<ProviderMediaDTO> createAvatar(@AuthenticationPrincipal Jwt jwt,
                                                      @RequestPart MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                customerService.uploadNewAvatar(jwt,file)
        );
    }
//    @GetMapping("/avatar")
//    public ResponseEntity<ProviderMediaDTO> fetchAvatar(@AuthenticationPrincipal Jwt jwt) {
//        return ResponseEntity.ok(customerService.getAvatarByJwt(jwt));
//    }
//
//    @PatchMapping("/avatar")
//    public ResponseEntity<ProviderMediaDTO> updateAvatar(@AuthenticationPrincipal Jwt jwt, @RequestPart MultipartFile file) {
//        log.debug("Updating provider resource : {}", file);
//        return ResponseEntity.ok(customerService.updateProviderAvatar(jwt,file));
//    }
//
//    @DeleteMapping("{mediaId}/avatar")
//    public ResponseEntity<String> deleteAvatar(@AuthenticationPrincipal Jwt jwt,
//                                               @PathVariable Long mediaId) {
//        log.debug("Deleting provider resource : {}", jwt);
//        return ResponseEntity.ok(customerService.deleteAvatarByMediaId(jwt,mediaId));
//    }


}