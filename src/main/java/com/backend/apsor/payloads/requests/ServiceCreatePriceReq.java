package com.backend.apsor.payloads.requests;

import com.backend.apsor.enums.ServicePriceBillingUnit;
import com.backend.apsor.enums.ServicePriceType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCreatePriceReq {

    @NotBlank(message = "name is required")
    @Size(max = 180, message = "name max length is 180")
    private String name;

    @NotNull(message = "priceType is required")
    private ServicePriceType priceType;

    @NotNull(message = "billingUnit is required")
    private ServicePriceBillingUnit billingUnit;

    /**
     * amount in minor units (e.g. cents). Can be null for QUOTE.
     */
    @Positive(message = "amount must be > 0")
    private Long amount;

    @Pattern(regexp = "^[A-Z]{3}$", message = "currency must be ISO-4217 (e.g., USD, KHR)")
    private String currency;

    @NotNull(message = "isDefault is required")
    private Boolean isDefault;

    @Min(value = 1, message = "minUnits must be >= 1")
    private Integer minUnits;

    @Min(value = 1, message = "maxUnits must be >= 1")
    private Integer maxUnits;

}
