package com.backend.apsor.entities;

import com.backend.apsor.enums.BusinessType;
import com.backend.apsor.enums.ProviderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Provider {

    @Id
    @Column(name = "provider_id", nullable = false, updatable = false)
    private Long id;

    // 1 user -> 1 provider profile
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_providers_users")
    )
    private Users user;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = Boolean.TRUE;

    @Column(name = "display_name", nullable = false, length = 120)
    private String displayName;

    @Column(name = "bio", length = 1000)
    private String bio;

    @Size(max = 150)
    private String businessName;

    @Enumerated(EnumType.STRING)
    @Column(name = "business_type", length = 20)
    private BusinessType businessType;

    private String establishedAt;

    @Column(name = "website_url", length = 250)
    private String websiteUrl;

    @Column(name = "telegram", length = 250)
    private String telegram;

    @Column(name = "facebook_url", length = 250)
    private String facebookUrl;

    @Column(name = "verified_at")
    private Instant verifiedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Enumerated(EnumType.STRING)
    private ProviderStatus status;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProviderMediaAsset> mediaAssets = new ArrayList<>();
}
