package com.backend.apsor.controller;

import com.backend.apsor.enums.UserType;
import com.backend.apsor.payloads.dtos.UserDTO;
import com.backend.apsor.payloads.requests.SignInReq;
import com.backend.apsor.payloads.requests.SignUpReq;
import com.backend.apsor.payloads.response.AuthResponse;
import com.backend.apsor.service.auth.AuthService;
import com.backend.apsor.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "SignUp/SignIn/Refresh/SignOut using HttpOnly cookies")
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    // -----------------------
    // Public Signup (Customer/Provider)
    // -----------------------
    @Operation(
            summary = "Signup",
            description = "Public signup. Default type=CUSTOMER. Use type=PROVIDER to create provider."
    )
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@Valid @RequestBody SignUpReq req,
                                          @RequestParam(defaultValue = "CUSTOMER") UserType type) {
        log.info("Received request to sign up user{}", req.getEmail());
        if (type == UserType.PROVIDER) {
            return ResponseEntity.status(201).body(userService.signUpProvider(req));
        }
        return ResponseEntity.status(201).body(userService.signUpCustomer(req));
    }

    @Operation(
            summary = "SignIn",
            description = "Sets HttpOnly cookies ACCESS_TOKEN and REFRESH_TOKEN. For Swagger testing, use Bearer token auth with a token from Keycloak token endpoint."
    )
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody SignInReq req, HttpServletResponse response) {
        log.info("Received request to sign in with: {}", req.getUsername());
        return ResponseEntity.ok(authService.login(req, response));
    }

    @Operation(
            summary = "Refresh",
            description = "Uses REFRESH_TOKEN cookie and returns new cookies.",
            // refresh is cookie-based; not bearer
            security = { @SecurityRequirement(name = "cookieAuth") }
    )
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.refresh(request, response));
    }

    @Operation(
            summary = "SignOut",
            description = "Clears cookies (and optionally calls Keycloak logout).",
            security = { @SecurityRequirement(name = "cookieAuth") }
    )
    @PostMapping("/signout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("REST request to logout");
        return ResponseEntity.ok(authService.logout(request, response));
    }

    // forgot password,email verification for future release
}

