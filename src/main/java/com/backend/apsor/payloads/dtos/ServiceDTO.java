package com.backend.apsor.payloads.dtos;

import com.backend.apsor.enums.ServiceLocationMode;
import com.backend.apsor.enums.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceDTO {
    private Long id;
    private String publicId;
    private String title;
    private String description;
    private Long categoryId;
    private Long subCategoryId;
    private Long providerId;
    private String slug;
    private ServiceLocationMode locationMode;
    private Instant publishedAt;
    private BigDecimal ratingAvg;
    private Integer ratingCount;
    private ServiceStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant suspendedAt;
}
