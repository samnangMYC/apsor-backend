package com.backend.apsor.entities;

import com.backend.apsor.enums.BusinessType;
import com.backend.apsor.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
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

    @Column(name = "display_name", nullable = false, length = 120)
    private String displayName;

    @Column(name = "bio", length = 1000)
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false, length = 20)
    private VerificationStatus verificationStatus = VerificationStatus.UNVERIFIED;

    @Column(name = "verification_note", length = 500)
    private String verificationNote;

    @Column(name = "verified_at")
    private Instant verifiedAt;

    @Column(name = "rating_avg", precision = 4, scale = 2)
    private BigDecimal ratingAvg; // e.g. 4.75

    @Column(name = "rating_count", nullable = false)
    private long ratingCount = 0L;

    @Column(name = "support_phone", nullable = false, length = 30)
    private String supportPhone;

    @Column(name = "support_email", nullable = false, length = 150)
    private String supportEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "business_type", length = 20)
    private BusinessType businessType;

    @Column(name = "company_name", length = 200)
    private String companyName;

    @Column(name = "website_url", length = 250)
    private String websiteUrl;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable = true;

    @Column(name = "available_from")
    private Instant availableFrom;

    @Column(name = "available_to")
    private Instant availableTo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}
