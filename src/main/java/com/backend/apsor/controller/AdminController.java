package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.UserDTO;
import com.backend.apsor.payloads.requests.CreateUserByAdminReq;
import com.backend.apsor.payloads.requests.SignUpReq;
import com.backend.apsor.payloads.requests.UpdateUserByAdminReq;
import com.backend.apsor.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/user")
//@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    // -----------------------
    // Admin (manage users)
    @PostMapping
    public ResponseEntity<UserDTO> create(@Valid @RequestBody CreateUserByAdminReq req) {
        log.info("Received request to create user by admin");
        return ResponseEntity.status(201).body(userService.createUserByAdmin(req));
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UpdateUserByAdminReq req) {
        return ResponseEntity.ok(userService.updateUserByAdmin(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/debug/authorities")
    public Object authorities(Authentication auth) {
        return auth.getAuthorities();
    }

}
