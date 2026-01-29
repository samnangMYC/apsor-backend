package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.UserDTO;
import com.backend.apsor.payloads.requests.AdminUpdateUserReq;
import com.backend.apsor.payloads.requests.SignUpReq;
import com.backend.apsor.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserService userService;

    // -----------------------
    // Admin (manage users)
    // -----------------------
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> createAdmin(@Valid @RequestBody SignUpReq req) {
        return ResponseEntity.status(201).body(userService.createAdmin(req));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> adminUpdate(@PathVariable Long id,
                                               @Valid @RequestBody AdminUpdateUserReq req) {
        return ResponseEntity.ok(userService.adminUpdate(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
