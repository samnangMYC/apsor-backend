package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.SubCategoryDTO;
import com.backend.apsor.payloads.requests.SubCategoryReq;
import com.backend.apsor.payloads.requests.SubCategoryStatusReq;
import com.backend.apsor.payloads.requests.SubCategoryUpdateReq;
import com.backend.apsor.service.SubCategoryService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/sub-categories")
@RequiredArgsConstructor
@Tag(
        name = "Sub Categories",
        description = "Role: PUBLIC. Endpoints for sub-category listing/management."
)
public class SubCategoryController {
    private final SubCategoryService subCategoryService;

    @PostMapping
    public ResponseEntity<SubCategoryDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request body for creating a category", required = true)
            @Valid @RequestBody SubCategoryReq req) {
        log.debug("REST request to save SubCategory : {}", req);
        return ResponseEntity.status(201).body(subCategoryService.createNewSubCategory(req));
    }

    @GetMapping
    public ResponseEntity<List<SubCategoryDTO>> findAll() {
        log.debug("REST request to get all SubCategories");
        return ResponseEntity.ok(subCategoryService.getAllSubCategory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubCategoryDTO> findById(
            @Parameter(description = "ID of the category to retrieve", required = true)
            @PathVariable Long id) {
        log.debug("REST request to get Category : {}", id);
        return ResponseEntity.ok(subCategoryService.getSubCategoryById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SubCategoryDTO> update(
            @Parameter(description = "ID of the category to update", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request body for updating a category", required = true)
            @Valid @RequestBody SubCategoryUpdateReq req) {
        log.debug("REST request to update Category : {}", req);
        return ResponseEntity.ok(subCategoryService.updateSubCategoryById(id, req));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SubCategoryDTO> updateStatus(
            @Parameter(description = "ID of the category to update status", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request body for updating category status", required = true)
            @RequestBody SubCategoryStatusReq req) {
        log.debug("REST request to update Category status : {}", req);
        return ResponseEntity.ok(subCategoryService.updateStatusById(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDelete(
            @Parameter(description = "ID of the category to soft delete", required = true)
            @PathVariable Long id) {
        log.debug("REST request to soft delete Category : {}", id);
        return ResponseEntity.ok(subCategoryService.softDeleteById(id));
    }
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<String> hardDelete(
            @Parameter(description = "ID of the category to hard delete", required = true)
            @PathVariable Long id) {
        log.debug("REST request to hard delete Category : {}", id);
        return ResponseEntity.ok(subCategoryService.deleteCategoryById(id));
    }
}
