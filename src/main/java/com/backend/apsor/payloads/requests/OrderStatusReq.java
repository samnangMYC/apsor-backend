package com.backend.apsor.payloads.requests;

import com.backend.apsor.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatusReq {

    @NotNull(message = "status cannot be null")
    private OrderStatus status;
}
