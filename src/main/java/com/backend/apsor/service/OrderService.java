package com.backend.apsor.service;

import com.backend.apsor.payloads.dtos.OrderDTO;
import com.backend.apsor.payloads.requests.OrderCreateReq;
import com.backend.apsor.payloads.requests.OrderStatusReq;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface OrderService {
    OrderDTO placeNewOrder(Jwt jwt, @Valid OrderCreateReq req);

    List<OrderDTO> getAllOrders();

    OrderDTO updateOrderStatus(Jwt jwt, Long orderId, OrderStatusReq req);

    String cancelOrder(Jwt jwt, Long orderId);
}
