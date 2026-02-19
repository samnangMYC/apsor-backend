package com.backend.apsor.payloads.dtos;

import com.backend.apsor.enums.ServicePriceBillingUnit;
import com.backend.apsor.enums.ServicePriceStatus;
import com.backend.apsor.enums.ServicePriceType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
@JsonPropertyOrder({
        "id",
        "name",
        "priceType",
        "billingUnit",
        "amount",
        "currency",
        "isDefault",
        "status",
        "minUnits",
        "maxUnits",
        "createdAt",
        "updatedAt"
})
public class ServicePriceDTO {

    private Long id;

    private String name;

    private ServicePriceType priceType;

    private ServicePriceBillingUnit billingUnit;

    private Long amount;

    private String currency;

    private Boolean isDefault;

    private ServicePriceStatus status;

    private Integer minUnits;

    private Integer maxUnits;

    private Instant createdAt;

    private Instant updatedAt;

}
