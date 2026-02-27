package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.CategoryDTO;
import com.backend.apsor.payloads.dtos.CategoryMediaDTO;
import com.backend.apsor.payloads.requests.CategoryReq;
import com.backend.apsor.payloads.requests.CategoryStatusReq;
import com.backend.apsor.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.description;

@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(
        name = "Categories",
        description = "Role: PUBLIC. Browse and read categories."
)
public class CategoryController {

    private final CategoryService categoryService;

    // create
    @PostMapping
    public ResponseEntity<CategoryDTO> create(
            @Valid @RequestBody CategoryReq req) {
        log.debug("REST request to save Category : {}", req);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createNewCategory(req));
    }

    // get all
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {
        log.debug("REST request to get all Categories");
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    // get by id
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable("id") Long categoryId) {
        log.debug("REST request to get Category : {}", categoryId);
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    // update
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(
            @PathVariable("id") Long categoryId,
            @Valid @RequestBody CategoryReq req) {
        log.debug("REST request to update Category : {}", req);
        return ResponseEntity.ok(categoryService.updateCategoryById(categoryId, req));
    }

    // update status
    @PatchMapping("/{id}/status")
    public ResponseEntity<CategoryDTO> updateStatus(
            @Parameter(description = "ID of the category to update status", required = true)
            @PathVariable("id") Long categoryId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request body for updating category status", required = true)
            @RequestBody CategoryStatusReq status) {
        log.debug("REST request to update Category status : {}", status);
        return ResponseEntity.ok(categoryService.updateStatusById(categoryId, status));
    }

    // soft delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDelete(
            @Parameter(description = "ID of the category to soft delete", required = true)
            @PathVariable("id") Long categoryId) {
        log.debug("REST request to soft delete Category : {}", categoryId);
        return ResponseEntity.ok(categoryService.softDeleteById(categoryId));
    }

    // hard delete
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<String> hardDelete(
            @Parameter(description = "ID of the category to hard delete", required = true)
            @PathVariable("id") Long categoryId) {
        log.debug("REST request to hard delete Category : {}", categoryId);
        return ResponseEntity.ok(categoryService.deleteCategoryById(categoryId));
    }

}
