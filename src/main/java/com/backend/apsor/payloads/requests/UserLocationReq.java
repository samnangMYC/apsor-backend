package com.backend.apsor.payloads.requests;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLocationReq {
    @NotBlank
    @Size(max = 200)
    private String line1;

    @Size(max = 200)
    private String line2;

    @Size(max = 80)
    private String district;

    @Size(max = 80)
    private String city;

    @Size(max = 80)
    private String province;

    @Size(max = 20)
    private String postalCode;

    @NotBlank
    @Size(min = 2, max = 2)
    private String countryCode;

    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private BigDecimal latitude;

    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private BigDecimal longitude;

    private Boolean isDefault;
}
