package com.backend.apsor.payloads.response;

import com.backend.apsor.enums.UserType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class AuthResponse {
    private String username;
    private String email;
    private UserType userTypes;
    private List<String> roles;
    private Instant expiresAt;
    private boolean success;
    private String message;
    private boolean emailVerified;
}