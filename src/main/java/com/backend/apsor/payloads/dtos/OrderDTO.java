package com.backend.apsor.payloads.dtos;

import com.backend.apsor.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private Long id;

    private String orderNo;

    private String currency;

    private Double subtotal;

    private Double discount;

    private Double total;

    private Integer units;

    private String note;

    private OrderStatus status;

    private Long servicePriceId;

    private Long serviceId;

    private Instant createdAt;

    private Instant updatedAt;
}
