package com.backend.apsor.entities;

import com.backend.apsor.enums.AvailabilityStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service_availabilities")
public class ServiceAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "service_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_service_availability_service")
    )
    private Services service;

    @Column(name = "timezone", nullable = false)
    @Builder.Default
    private String timezone = "Asia/Phnom_Penh";

    @Min(1) @Max(127)
    @Column(name = "open_days_mask", nullable = false)
    @Builder.Default
    private Integer openDaysMask = 31; // Mon-Fri default

    @Column(name = "start_time", nullable = false)
    @Builder.Default
    private LocalTime startTime = LocalTime.of(9, 0);

    @Column(name = "end_time", nullable = false)
    @Builder.Default
    private LocalTime endTime = LocalTime.of(17, 0);

    @Min(5) @Max(1440)
    @Column(name = "slot_duration_minutes", nullable = false)
    @Builder.Default
    private Integer slotDurationMinutes = 30;

    @Min(1)
    @Column(name = "capacity_per_slot", nullable = false)
    @Builder.Default
    private Integer capacityPerSlot = 1;

    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private Boolean isDefault = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 24)

    @Builder.Default
    private AvailabilityStatus status = AvailabilityStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    @PreUpdate
    void validateAndForceCambodia() {
        this.timezone = "Asia/Phnom_Penh";

        if (startTime == null || endTime == null || !endTime.isAfter(startTime)) {
            throw new IllegalStateException("endTime must be after startTime");
        }
        if (openDaysMask == null || openDaysMask < 1 || openDaysMask > 127) {
            throw new IllegalStateException("openDaysMask must be between 1 and 127");
        }
    }

}
