package com.backend.apsor.entities;

import com.backend.apsor.enums.MediaPurpose;
import com.backend.apsor.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class ServiceMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MediaPurpose mediaPurpose;

    @Column(nullable = false)
    private Boolean isPrimary = false;

    private int sortOrder = 0;

    @CreationTimestamp
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_service_media_service"))
    private Services service;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "media_asset_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_service_media_asset"))
    private MediaAsset mediaAsset;

}
