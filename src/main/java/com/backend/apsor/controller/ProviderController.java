package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.MediaAssetDTO;
import com.backend.apsor.payloads.dtos.ProviderDTO;
import com.backend.apsor.payloads.dtos.ProviderMediaDTO;
import com.backend.apsor.payloads.requests.ProviderReq;
import com.backend.apsor.service.ProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @PostMapping
    public ResponseEntity<ProviderDTO> create(@AuthenticationPrincipal Jwt jwt,
                                              @RequestBody ProviderReq req) {
        log.debug("Creating provider : {}", req);
        return ResponseEntity.status(HttpStatus.CREATED).body(providerService.createNewProviderByJwt(jwt,req));
    }

    @GetMapping
    public ResponseEntity<ProviderDTO> fetch(@AuthenticationPrincipal Jwt jwt){
        log.debug("Fetching provider : {}", jwt);
        return ResponseEntity.ok(providerService.getProviderByJwt(jwt));
    }

    @PatchMapping
    public ResponseEntity<ProviderDTO> update(@AuthenticationPrincipal Jwt jwt, @RequestBody ProviderReq req) {
        log.debug("Updating provider : {}", req);
        return ResponseEntity.ok(providerService.updateProviderByJwt(jwt,req));
    }

    @PostMapping("/avatar")
    public ResponseEntity<MediaAssetDTO> createAvatar(@AuthenticationPrincipal Jwt jwt,
                                                @RequestPart MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                providerService.uploadNewAvatar(jwt,file)
        );
    }
    @GetMapping("/avatar")
    public ResponseEntity<ProviderMediaDTO> fetchAvatar(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(providerService.getAvatarByJwt(jwt));
    }

    @PatchMapping("/avatar")
    public ResponseEntity<ProviderMediaDTO> updateAvatar(@AuthenticationPrincipal Jwt jwt, @RequestPart MultipartFile file) {
        log.debug("Updating provider resource : {}", file);
        return ResponseEntity.ok(providerService.updateProviderAvatar(jwt,file));
    }

    @DeleteMapping("{mediaId}/avatar")
    public ResponseEntity<String> deleteAvatar(@AuthenticationPrincipal Jwt jwt,
                                                         @PathVariable Long mediaId) {
        log.debug("Deleting provider resource : {}", jwt);
        return ResponseEntity.ok(providerService.deleteAvatarByMediaId(jwt,mediaId));
    }
}
