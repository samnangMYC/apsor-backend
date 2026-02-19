package com.backend.apsor.entities;

import com.backend.apsor.enums.ServicePriceBillingUnit;
import com.backend.apsor.enums.ServicePriceStatus;
import com.backend.apsor.enums.ServicePriceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "service_prices")
public class  ServicePrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_service_price_service"))
    private Services service;

    @Column(name = "name", nullable = false, length = 120)
    private String name; // e.g. "Basic", "Hourly", "Starter Monthly"

    @Enumerated(EnumType.STRING)
    @Column(name = "price_type", nullable = false, length = 20)
    private ServicePriceType priceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_unit", nullable = false, length = 20)
    private ServicePriceBillingUnit billingUnit;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency; // ISO 4217: USD, KHR

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private ServicePriceStatus status;

    @Column(name = "min_units")
    private Integer minUnits = 1;

    @Column(name = "max_units")
    private Integer maxUnits = 1;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

//    @OneToMany(mappedBy = "service",fetch = FetchType.LAZY)
//    @Builder.Default
//    private List<Order> orders = new ArrayList<>();


}
