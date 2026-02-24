package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.UserDTO;
import com.backend.apsor.payloads.requests.CreateUserByAdminReq;
import com.backend.apsor.payloads.requests.UpdateUserByAdminReq;
import com.backend.apsor.payloads.requests.UserTypeReq;
import com.backend.apsor.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Manage Users By Admin", description = "Endpoints for administrators to manage users.")
@Slf4j
@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @Operation(summary = "Create a new user by admin")
    @PostMapping
    public ResponseEntity<UserDTO> create(@AuthenticationPrincipal Jwt jwt,
                                          HttpServletRequest request,
                                          @Valid @RequestBody CreateUserByAdminReq req) {
        log.info("Received request to create user by admin");
        UserDTO created = userService.createUserByAdmin(req);

        return ResponseEntity.status(201).body(created);
    }

    @Operation(summary = "Get user by ID from admin")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserByIdFromAdmin(@AuthenticationPrincipal Jwt jwt,
                                                        HttpServletRequest request,
                                                        @PathVariable("id") Long userId) {
        log.info("Received request to get the user by admin");
        UserDTO dto = userService.getUserByIdFromAdmin(userId);

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get all users from admin")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUserFromAdmin(@AuthenticationPrincipal Jwt jwt,
                                                             HttpServletRequest request) {
        log.info("Received request to get all users");
        List<UserDTO> list = userService.getAllUserFromAdmin();

        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Update user by admin")
    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@AuthenticationPrincipal Jwt jwt,
                                              HttpServletRequest request,
                                              @PathVariable("id") Long userId,
                                              @RequestBody UpdateUserByAdminReq req) {
        UserDTO updated = userService.updateUserByAdmin(userId, req);

        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Update user type by admin")
    @PatchMapping("/{id}/user-type")
    public ResponseEntity<UserDTO> updateUserType(@AuthenticationPrincipal Jwt jwt,
                                                  HttpServletRequest request,
                                                  @PathVariable("id") Long userId,
                                                  @Valid @RequestBody UserTypeReq req) {
        UserDTO updated = userService.updateUserTypeByAdmin(userId, req);

        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Soft delete user by admin")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDelete(@AuthenticationPrincipal Jwt jwt,
                                             HttpServletRequest request,
                                             @PathVariable("id") Long userId) {
        String msg = userService.softDeleteUserByAdmin(userId);

        return ResponseEntity.ok(msg);
    }

    @Operation(summary = "Hard delete user by admin")
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<String> hardDelete(@AuthenticationPrincipal Jwt jwt,
                                             HttpServletRequest request,
                                             @PathVariable("id") Long userId) {
        String msg = userService.hardDeleteUserByAdmin(userId);

        return ResponseEntity.ok(msg);
    }

    @GetMapping("/debug/authorities")
    public Object authorities(Authentication auth) {
        return auth.getAuthorities();
    }
}