package com.backend.apsor.controller;

import com.backend.apsor.payloads.requests.SignUpReq;
import com.backend.apsor.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpReq req){
        return ResponseEntity.ok(userService.registerNewUser(req));
    }
}
