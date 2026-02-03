package com.backend.apsor.payloads.requests;

import com.backend.apsor.enums.Gender;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerReq {
    /**
     * Date of birth (optional in many apps). If you require it, add @NotNull.
     */
    @Past(message = "date must be in the past")
    private LocalDate dob;

    /**
     * If you require gender, add @NotNull.
     */
    private Gender gender;

    /**
     * ISO language tag like: en, km, en-US, km-KH
     */
    @Size(max = 10, message = "preferredLanguage must be at most 10 characters")
    @Pattern(
            regexp = "^[a-zA-Z]{2,3}(-[a-zA-Z]{2})?$",
            message = "preferredLanguage must be a valid language tag (e.g., en, km, en-US)"
    )
    private String preferredLanguage;

    @Size(max = 1000, message = "bio must be at most 1000 characters")
    private String bio;

    /**
     * Keep boolean flags non-null to avoid null handling bugs.
     */
    @NotNull(message = "onboardingCompleted is required")
    private Boolean onboardingCompleted = Boolean.FALSE;


}
