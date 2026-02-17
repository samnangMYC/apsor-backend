package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.CategoryDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.description;

@Slf4j
@RestController
@RequestMapping("/api/v1/public/categories")
@RequiredArgsConstructor
@Tag(
        name = "Categories",
        description = "Role: PUBLIC. Browse and read categories."
)
public class CategoryController {

    private final CategoryService categoryService;

    // create
    @Operation(summary = "Create a new category",
            description = "Creates a new category with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CategoryDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request body for creating a category", required = true)
            @Valid @RequestBody CategoryReq req) {
        log.debug("REST request to save Category : {}", req);
        return ResponseEntity.status(201).body(categoryService.createNewCategory(req));
    }

    // get all
    @Operation(summary = "Get all categories",
            description = "Retrieves a list of all categories.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of categories retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {
        log.debug("REST request to get all Categories");
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    // get by id
    @Operation(summary = "Get category by ID",
            description = "Retrieves a specific category by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class)) }),
    })
    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDTO> findById(
            @Parameter(description = "ID of the category to retrieve", required = true)
            @PathVariable("catId") Long id) {
        log.debug("REST request to get Category : {}", id);
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    // update
    @Operation(summary = "Update category by ID",
            description = "Partially updates an existing category's details by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class)) }),
    })
    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDTO> update(
            @Parameter(description = "ID of the category to update", required = true)
            @PathVariable("catId") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request body for updating a category", required = true)
            @Valid @RequestBody CategoryReq req) {
        log.debug("REST request to update Category : {}", req);
        return ResponseEntity.ok(categoryService.updateCategoryById(id, req));
    }

    // update status
    @Operation(summary = "Update category status by ID",
            description = "Updates the status of an existing category by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category status updated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class)) }),
    })
    @PatchMapping("/{catId}/status")
    public ResponseEntity<CategoryDTO> updateStatus(
            @Parameter(description = "ID of the category to update status", required = true)
            @PathVariable("catId") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request body for updating category status", required = true)
            @RequestBody CategoryStatusReq status) {
        log.debug("REST request to update Category status : {}", status);
        return ResponseEntity.ok(categoryService.updateStatusById(id, status));
    }

    // soft delete
    @Operation(summary = "Soft delete category by ID",
            description = "Performs a soft delete on a category, marking it as deleted without removing data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category soft deleted successfully",
                    content = { @Content(mediaType = "text/plain") }),
    })
    @DeleteMapping("/{catId}")
    public ResponseEntity<String> softDelete(
            @Parameter(description = "ID of the category to soft delete", required = true)
            @PathVariable("catId") Long id) {
        log.debug("REST request to soft delete Category : {}", id);
        return ResponseEntity.ok(categoryService.softDeleteById(id));
    }
    // hard delete
    @Operation(summary = "Hard delete category by ID",
            description = "Permanently deletes a category and its data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category hard deleted successfully",
                    content = { @Content(mediaType = "text/plain") }),
    })
    @DeleteMapping("/{catId}/hard")
    public ResponseEntity<String> hardDelete(
            @Parameter(description = "ID of the category to hard delete", required = true)
            @PathVariable("catId") Long id) {
        log.debug("REST request to hard delete Category : {}", id);
        return ResponseEntity.ok(categoryService.deleteCategoryById(id));
    }
}
