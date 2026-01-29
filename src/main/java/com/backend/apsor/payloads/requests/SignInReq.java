package com.backend.apsor.payloads.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInReq {
    @NotBlank
    private String username; // can be email if Keycloak "Login with email" enabled

    @NotBlank
    private String password;
}
