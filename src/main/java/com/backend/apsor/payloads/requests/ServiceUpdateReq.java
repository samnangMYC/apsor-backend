package com.backend.apsor.payloads.requests;

import com.backend.apsor.enums.ServiceLocationMode;
import com.backend.apsor.enums.ServiceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceUpdateReq {
    @NotBlank(message = "Title must not be blank")  // Best practice: Add custom message for better error handling
    @Size(max = 160, message = "Title must be at most 160 characters")
    private String title;

    @Size(max = 2000, message = "Description must be at most 2000 characters")
    private String description;  // No @NotBlank added; assuming description is optional. If required, add @NotBlank.

    @NotNull(message = "Location mode must not be null")  // Best practice: Make enum required if it shouldn't be null
    private ServiceLocationMode locationMode;

    @NotBlank(message = "Status is required")
    private ServiceStatus status;


}
