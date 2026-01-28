package com.backend.apsor.controller;

import com.backend.apsor.enums.UserType;
import com.backend.apsor.payloads.dtos.UserDTO;
import com.backend.apsor.payloads.requests.AdminUpdateUserReq;
import com.backend.apsor.payloads.requests.SignUpReq;
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
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // -----------------------
    // Public Signup (Customer/Provider)
    // -----------------------
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@Valid @RequestBody SignUpReq req,
                                          @RequestParam(defaultValue = "CUSTOMER") UserType type) {
        if (type == UserType.PROVIDER) {
            return ResponseEntity.status(201).body(userService.signUpProvider(req));
        }
        return ResponseEntity.status(201).body(userService.signUpCustomer(req));
    }


    // -----------------------
    // Auth (cookies)
    // -----------------------
//    @PostMapping("/auth/login")
//    public ResponseEntity<Void> login(@Valid @RequestBody LoginReq req, HttpServletResponse response) {
//        authService.login(req, response);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/auth/refresh")
//    public ResponseEntity<Void> refresh(HttpServletRequest request, HttpServletResponse response) {
//        authService.refresh(request, response);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/auth/logout")
//    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
//        authService.logout(request, response);
//        return ResponseEntity.noContent().build();
//    }


}

