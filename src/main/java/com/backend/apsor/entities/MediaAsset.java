package com.backend.apsor.entities;

import com.backend.apsor.enums.MediaPurpose;
import com.backend.apsor.enums.MediaType;
import com.backend.apsor.enums.MediaVisibility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "media_assets", indexes = {
        @Index(name = "ix_media_provider", columnList = "provider_id"),
//        @Index(name = "ix_media_customer", columnList = "customer_id"),
//        @Index(name = "ix_media_service", columnList = "service_id"),
})
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MediaAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MediaType mediaType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MediaPurpose purpose;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MediaVisibility visibility = MediaVisibility.PUBLIC;

    @Column(nullable = false, length = 1200)
    private String url;

    @Column(length = 255)
    private String title;

    @Column(nullable = false)
    private Boolean isPrimary = Boolean.FALSE;

    @Column(nullable = false)
    private Integer sortOrder = 0;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "provider_id")
    private Provider provider;

}
