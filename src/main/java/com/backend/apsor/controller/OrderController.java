package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.OrderDTO;
import com.backend.apsor.payloads.requests.OrderCreateReq;
import com.backend.apsor.payloads.requests.OrderStatusReq;
import com.backend.apsor.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping
    public ResponseEntity<OrderDTO> create(@AuthenticationPrincipal Jwt jwt,
                                           @Valid @RequestBody OrderCreateReq req) {
        log.info("Creating order from customer: {}", req);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeNewOrder(jwt,req));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> fetchAll(){
        log.info("Fetching all orders");
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PatchMapping("{id}/status")
    public ResponseEntity<OrderDTO> status(@AuthenticationPrincipal Jwt jwt,
                                           @PathVariable("id") Long orderId,
                                           OrderStatusReq req){
        log.info("Fetching orders from customer");
        return ResponseEntity.ok(orderService.updateOrderStatus(jwt,orderId,req));
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PatchMapping("{id}/cancel")
    public ResponseEntity<String> cancel(@AuthenticationPrincipal Jwt jwt,
                                         @PathVariable("id") Long orderId){
        log.info("Cancelling order from customer");
        return ResponseEntity.ok(orderService.cancelOrder(jwt,orderId));
    }

}
