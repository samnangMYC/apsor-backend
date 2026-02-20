package com.backend.apsor.payloads.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceLocationDTO {

    private Long id;

    private String line1;

    private String line2;

    private String district;

    private String city;

    private String province;

    private String postalCode;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private Boolean isDefault;

    private Instant createdAt;

    private Instant updatedAt;

}
