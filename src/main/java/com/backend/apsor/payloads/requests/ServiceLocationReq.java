package com.backend.apsor.payloads.requests;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceLocationReq {

    private Long serviceId;

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

    @NotNull(message = "is default is required")
    private Boolean isDefault;
}
