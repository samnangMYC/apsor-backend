package com.backend.apsor.payloads.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateReq {

    @NotNull(message = "Service ID is required")
    @Positive(message = "Service ID must be positive")
    private Long serviceId;

    @NotNull(message = "Service Price ID is required")
    @Positive(message = "Service Price ID must be positive")
    private Long servicePriceId;

    @NotNull(message = "Subtotal is required")
    @PositiveOrZero(message = "Subtotal must be non-negative")
    private Double subtotal;

    @NotNull(message = "Discount is required")
    @PositiveOrZero(message = "Discount must be non-negative")
    private Double discount;

    @NotNull(message = "Units is required")
    @Positive(message = "Units must be positive")
    private Integer units;

    @Size(max = 500, message = "Note cannot exceed 500 characters")
    private String note;

}
