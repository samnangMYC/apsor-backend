package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.ServiceMediaDTO;
import com.backend.apsor.payloads.requests.UpdateSortOrderReq;
import com.backend.apsor.service.ServiceMediaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
@Tag(
        name = "Service Media",
        description = "Role: PROVIDER. Upload/manage media for services."
)
@SecurityRequirement(name = "bearerAuth")
public class ServiceMediaController {

    private final ServiceMediaService serviceMediaService;

    // create thumbnail
    @PostMapping(value = "/{id}/gallery", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ServiceMediaDTO>> gallery(
            @PathVariable("id") Long serviceId,
            @RequestPart("files") List<MultipartFile> files
    ) {
        log.debug("REST request to upload gallery files count={}", files.size());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceMediaService.uploadNewGallery(serviceId, files));
    }


    @GetMapping("/{id}/gallery")
    public ResponseEntity<List<ServiceMediaDTO>> gallery(@AuthenticationPrincipal Jwt jwt,
                                                         @PathVariable("id") Long serviceId) {
        log.debug("REST request to save ServiceMedia : ");
        return ResponseEntity.ok(serviceMediaService.getAllGallery(jwt,serviceId));

    }

    @PatchMapping("/{serviceId}/gallery/{serviceMediaId}/primary")
    public ResponseEntity<ServiceMediaDTO> thumbnail(
            @PathVariable Long serviceId,
            @PathVariable Long serviceMediaId
    ) {
        return ResponseEntity.ok(serviceMediaService.setGalleryPrimary(serviceId, serviceMediaId));
    }

    @PatchMapping("/{serviceId}/gallery/{serviceMediaId}/sort-order")
    public ResponseEntity<ServiceMediaDTO> updateSortOrder(
            @PathVariable Long serviceId,
            @PathVariable Long serviceMediaId,
            @RequestBody UpdateSortOrderReq req
    ) {
        return ResponseEntity.ok(serviceMediaService.updateGallerySortOrder(serviceId, serviceMediaId, req.sortOrder()));
    }

    @PutMapping(value = "/{serviceId}/gallery/{serviceMediaId}/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ServiceMediaDTO> replaceFile(
            @PathVariable Long serviceId,
            @PathVariable Long serviceMediaId,
            @RequestPart("file") MultipartFile file
    ) {
        return ResponseEntity.ok(serviceMediaService.replaceGalleryFile(serviceId, serviceMediaId, file));
    }

    @DeleteMapping("/{serviceId}/gallery/{serviceMediaId}/hard")
    public ResponseEntity<String> hardDeleteGalleryItem(
            @PathVariable Long serviceId,
            @PathVariable Long serviceMediaId
    ) {
        serviceMediaService.hardDeleteGalleryItem(serviceId, serviceMediaId);
        return ResponseEntity.noContent().build();
    }


}
