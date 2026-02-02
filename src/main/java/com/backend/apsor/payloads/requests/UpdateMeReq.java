package com.backend.apsor.payloads.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMeReq {
    @Size(max = 50, message = "First name must be at most 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name must be at most 50 characters")
    private String lastName;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must be at most 100 characters")
    private String email;

    @Pattern(regexp = "^[+]?[0-9]{1,15}$", message = "Phone number should be valid (international format, up to 15 digits)")
    private String phoneNumber;
}
