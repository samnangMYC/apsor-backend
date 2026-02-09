package com.backend.apsor.payloads.requests;

import com.backend.apsor.enums.ProviderStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderStatusReq {
    @NotBlank(message = "Provider status is required")
    private ProviderStatus status;
}
