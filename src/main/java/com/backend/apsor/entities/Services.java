package com.backend.apsor.entities;

import com.backend.apsor.enums.ServiceLocationMode;
import com.backend.apsor.enums.ServiceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service")
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Public immutable identifier for APIs.
     */
    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    private UUID publicId;

    @Column(name = "title", nullable = false, length = 160)
    private String title;

    /**
     * Slug used for URLs. Keep unique per provider (enforce case-insensitive in DB migration).
     */
    @Column(name = "slug", nullable = false, length = 180)
    private String slug;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_mode", nullable = false, length = 16)
    private ServiceLocationMode locationMode;

    @Builder.Default
    @Column(name = "rating_avg", precision = 3, scale = 2)
    private BigDecimal ratingAvg = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "rating_count", nullable = false)
    private Integer ratingCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private ServiceStatus status;

    @Column(name = "published_at")
    private Instant publishedAt;

    // --- audit / lifecycle ---
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "suspended_at")
    private Instant suspendedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "provider_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_services_provider"))
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_services_category"))
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id",
            foreignKey = @ForeignKey(name = "fk_services_subcategory"))
    private SubCategory subcategory;

    @OneToMany(mappedBy = "service",fetch = FetchType.LAZY,orphanRemoval = true)
    @Builder.Default
    private List<ServiceLocation> locations = new ArrayList<>();

    @OneToMany(mappedBy = "service",fetch = FetchType.LAZY,orphanRemoval = true)
    @Builder.Default
    private List<ServiceMedia> medias = new ArrayList<>();

    @OneToMany(mappedBy = "service",fetch = FetchType.LAZY,orphanRemoval = true)
    @Builder.Default
    private List<ServicePrice> prices = new ArrayList<>();

}
