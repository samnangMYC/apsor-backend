package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.AdminProviderDTO;
import com.backend.apsor.payloads.dtos.MediaAssetDTO;
import com.backend.apsor.payloads.dtos.ProviderMediaDTO;
import com.backend.apsor.payloads.requests.AdminProviderReq;
import com.backend.apsor.payloads.requests.ProviderStatusReq;
import com.backend.apsor.service.ProviderMediaService;
import com.backend.apsor.service.ProviderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.ws.rs.PathParam;
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
@Tag(
        name = "Admin - Providers",
        description = "Role: ADMIN. Admin management of providers."
)
@SecurityRequirement(name = "bearerAuth")
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

    @PostMapping("/{providerId}/avatar")
    public ResponseEntity<ProviderMediaDTO> avatar(@PathVariable Long providerId,@RequestPart MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                providerMediaService.uploadNewAvatarFromAdmin(providerId,file)
              );
    }
    // get lastest image
    @GetMapping("/{providerId}/avatar")
    public ResponseEntity<ProviderMediaDTO> getById(@PathVariable Long providerId) {
        return ResponseEntity.ok(providerMediaService.getAvatarByIdFromAdmin(providerId));
    }
    @PatchMapping("/{providerId}/avatar")
    public ResponseEntity<ProviderMediaDTO> updateAvatar(@PathVariable Long providerId,
                                                      @RequestPart MultipartFile file) {
        log.debug("REST request to update Provider avatar : {}", file.getOriginalFilename());
        return ResponseEntity.ok(providerMediaService.updateNewAvatarFromAdmin(providerId,file));

    }
    @DeleteMapping("{providerId}/avatar/{mediaId}")
    public ResponseEntity<String> delete(@PathVariable Long providerId,@PathVariable Long mediaId ) {
        log.debug("REST request to delete Provider Avatar : {}", providerId);
        return ResponseEntity.ok(providerService.deleteAvatarByIdFromAdmin(providerId,mediaId));
    }


}
