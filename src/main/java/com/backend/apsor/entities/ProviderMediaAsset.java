package com.backend.apsor.entities;

import com.backend.apsor.enums.MediaPurpose;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "provider_media_assets",
        indexes = {
                @Index(name = "ix_pma_provider", columnList = "provider_id"),
                @Index(name = "ix_pma_media", columnList = "media_id")
        }
)
public class ProviderMediaAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "provider_media_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "provider_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_pma_provider"))
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "media_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_pma_media"))
    private MediaAsset media;

    @Enumerated(EnumType.STRING)
    @Column(name = "purpose", nullable = false, length = 30)
    private MediaPurpose purpose;

    @Column(name = "sort_order", nullable = false)
    @Builder.Default
    private int sortOrder = 0;
}