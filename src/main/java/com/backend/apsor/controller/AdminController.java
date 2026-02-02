package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.UserDTO;
import com.backend.apsor.payloads.requests.CreateUserByAdminReq;
import com.backend.apsor.payloads.requests.SignUpReq;
import com.backend.apsor.payloads.requests.UpdateUserByAdminReq;
import com.backend.apsor.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Manage Users By Admin", description = "Endpoints for administrators to manage users, including creation, retrieval, update, and deletion operations.")
@Slf4j
@RestController
@RequestMapping("/api/v1/admin/user")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    // -----------------------
    // Admin (manage users)
    // -----------------------
    @Operation(summary = "Create a new user by admin",
            description = "Allows an admin to create a new user account with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<UserDTO> create(@Valid @RequestBody CreateUserByAdminReq req) {
        log.info("Received request to create user by admin");
        return ResponseEntity.status(201).body(userService.createUserByAdmin(req));
    }

    @Operation(summary = "Get user by ID from admin",
            description = "Retrieves a specific user by their ID for admin purposes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserByIdFromAdmin(@PathVariable Long id) {
        log.info("Received request to get the user by admin");
        return ResponseEntity.ok(userService.getUserByIdFromAdmin(id));

    }

    @Operation(summary = "Get all users from admin",
            description = "Retrieves a list of all users for admin review.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUserFromAdmin(){
        log.info("Received request to get all users");
        return ResponseEntity.ok(userService.getAllUserFromAdmin());
    }

    @Operation(summary = "Update user by admin",
            description = "Updates an existing user's details by admin.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UpdateUserByAdminReq req) {
        return ResponseEntity.ok(userService.updateUserByAdmin(id, req));
    }

    // Soft delete (recommended default)
    @Operation(summary = "Soft delete user by admin",
            description = "Performs a soft delete on a user, marking them as deleted without removing data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User soft deleted successfully",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        return ResponseEntity.ok(userService.softDeleteUserByAdmin(id));
    }

    // Hard delete (explicit)
    @Operation(summary = "Hard delete user by admin",
            description = "Permanently deletes a user and their data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User hard deleted successfully",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<String> hardDelete(@PathVariable Long id) {
        return ResponseEntity.ok(userService.hardDeleteUserByAdmin(id));
    }

    @Operation(summary = "Debug user authorities",
            description = "Returns the authorities of the authenticated user for debugging purposes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authorities retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/debug/authorities")
    public Object authorities(Authentication auth) {
        return auth.getAuthorities();
    }

}
