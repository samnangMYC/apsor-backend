package com.backend.apsor.controller;

import com.backend.apsor.payloads.response.VerifyEmailStatusRes;
import com.backend.apsor.service.VerifyEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class VerifyEmailController {

    private final VerifyEmailService verifyEmailService;

    @PostMapping("/verify-email/resend")
    public ResponseEntity<Void> resend(@AuthenticationPrincipal Jwt jwt) {
        verifyEmailService.resendForCurrentUser(jwt);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verify-email/status")
    public ResponseEntity<VerifyEmailStatusRes> status(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(new VerifyEmailStatusRes(verifyEmailService.isEmailVerified(jwt)));
    }

}
