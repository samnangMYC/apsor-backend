package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.UserDTO;
import com.backend.apsor.payloads.requests.AdminUpdateUserReq;
import com.backend.apsor.payloads.requests.UpdateMeReq;
import com.backend.apsor.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // -----------------------
    // Me (any logged-in user)
    // -----------------------
    @GetMapping("/me")
    public ResponseEntity<UserDTO> me(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(userService.getMe(jwt));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserDTO> updateMe(@AuthenticationPrincipal Jwt jwt,
                                            @Valid @RequestBody UpdateMeReq req) {
        return ResponseEntity.ok(userService.updateMe(jwt, req));
    }


}
