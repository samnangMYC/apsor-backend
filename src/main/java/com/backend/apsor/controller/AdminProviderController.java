package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.AdminProviderDTO;
import com.backend.apsor.payloads.dtos.MediaAssetDTO;
import com.backend.apsor.payloads.dtos.ProviderMediaDTO;
import com.backend.apsor.payloads.requests.AdminProviderReq;
import com.backend.apsor.payloads.requests.ProviderStatusReq;
import com.backend.apsor.service.ProviderMediaService;
import com.backend.apsor.service.ProviderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/providers")
@RequiredArgsConstructor
public class AdminProviderController {

    private final ProviderService providerService;
    private final ProviderMediaService providerMediaService;

    @PostMapping
    public ResponseEntity<AdminProviderDTO> create(@Valid @RequestBody AdminProviderReq req){
        log.debug("REST request to save Provider : {}", req);
        return ResponseEntity.status(HttpStatus.CREATED).body(providerService.createNewProviderByAdmin(req));
    }

    @GetMapping
    public ResponseEntity<List<AdminProviderDTO>> findAll(){
        log.debug("REST request to get all Providers");
        return ResponseEntity.ok(providerService.getAllProviderByAdmin());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminProviderDTO> findById(@PathVariable Long id){
        log.debug("REST request to get Provider : {}", id);
        return ResponseEntity.ok(providerService.getProviderByIdFromAdmin(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AdminProviderDTO> update(@PathVariable Long id, @Valid @RequestBody AdminProviderReq req){
        log.debug("REST request to update Provider : {}", req);
        return ResponseEntity.ok(providerService.updateProviderByIdFromAdmin(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AdminProviderDTO> updateStatus(@PathVariable Long id, @Valid @RequestBody ProviderStatusReq req){
        log.debug("REST request to update Provider status : {}", req);
        return ResponseEntity.ok(providerService.updateStatusByIdFromAdmin(id,req));
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<String> hardDelete(@PathVariable Long id){
        log.debug("REST request to delete Provider : {}", id);

        return ResponseEntity.ok(providerService.deleteProviderByIdFromAdmin(id));
    }

    @PostMapping("/{providerId}/avartar")
    public ResponseEntity<MediaAssetDTO> avatar(@PathVariable Long providerId,@RequestPart MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                providerMediaService.uploadNewAvatarFromAdmin(providerId,file)
              );
    }
    @GetMapping("/{providerId}/avartar")
    public ResponseEntity<ProviderMediaDTO> getById(@PathVariable Long providerId) {
        return ResponseEntity.ok(providerMediaService.getAvatarByIdFromAdmin(providerId));

    }

}
