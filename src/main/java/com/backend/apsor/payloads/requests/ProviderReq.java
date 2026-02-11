package com.backend.apsor.payloads.requests;

import com.backend.apsor.enums.BusinessType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderReq {
    @NotBlank(message = "Display name is required")
    @Size(max = 120, message = "Display name must be at most 120 characters")
    private String displayName;

    @Size(max = 1000, message = "Bio must be at most 1000 characters")
    private String bio;

    @Size(max = 150, message = "Business name must be at most 150 characters")
    private String businessName;

    @NotNull(message = "Business type is required")
    private BusinessType businessType;

    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$", message = "Established at must be in yyyy-MM-dd format")
    private String establishedAt;

    @URL(message = "Invalid website URL")
    @Size(max = 250, message = "Website URL must be at most 250 characters")
    private String websiteUrl;

    @URL(message = "Invalid Facebook URL")
    @Size(max = 250, message = "Facebook URL must be at most 250 characters")
    private String facebookUrl;

    @Size(max = 30, message = "Telegram must be at most 30 characters")
    @Pattern(regexp = "^[+]?[0-9\\s()-]{1,30}$", message = "Invalid Telegram phone number format")
    private String telegram;
}
