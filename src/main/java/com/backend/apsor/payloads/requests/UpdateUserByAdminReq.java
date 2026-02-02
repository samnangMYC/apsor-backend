package com.backend.apsor.payloads.requests;

import com.backend.apsor.enums.UserStatus;
import com.backend.apsor.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
public class UpdateUserByAdminReq {
    @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
    private String username;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must be at most 100 characters")
    private String email;

    @Size(max = 50, message = "First name must be at most 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name must be at most 50 characters")
    private String lastName;

    @NotNull(message = "User type is required")
    private UserType userType;

    @NotNull(message = "Status is required")
    private UserStatus status;

    @Pattern(regexp = "^[+]?[0-9]{1,15}$", message = "Phone number should be valid (international format, up to 15 digits)")
    private String phoneNumber;

    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters if provided")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, one special character, and no whitespace")
    private String password;
}
