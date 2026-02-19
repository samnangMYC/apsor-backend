package com.backend.apsor.entities;

import com.backend.apsor.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", nullable = false, length = 40)
    private String orderNo;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @Column(name = "discount", nullable = false)
    private Double discount;

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "units")
    private Integer units;

    @Column(name = "note", columnDefinition = "text")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_price_id")
    private ServicePrice servicePrice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private Services service;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}
