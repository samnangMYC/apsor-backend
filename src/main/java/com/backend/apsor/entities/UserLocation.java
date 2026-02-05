package com.backend.apsor.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "user_location",
        indexes = {
                @Index(name = "idx_locations_user_id", columnList = "user_id"),
                @Index(name = "idx_ul_user_default", columnList = "user_id,is_default")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_locations_users")
    )
    private Users user;

    @Column(name = "line1", length = 200, nullable = false)
    private String line1;

    @Column(name = "line2", length = 200)
    private String line2;

    @Column(name = "district", length = 80)
    private String district;

    @Column(name = "city", length = 80)
    private String city;

    @Column(name = "province", length = 80)
    private String province;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = Boolean.FALSE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

}
