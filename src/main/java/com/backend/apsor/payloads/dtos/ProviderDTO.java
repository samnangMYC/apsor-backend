package com.backend.apsor.payloads.dtos;

import com.backend.apsor.entities.Users;
import com.backend.apsor.enums.BusinessType;
import com.backend.apsor.enums.ProviderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderDTO {

    private Long id;

    private UserDTO user;

    private Boolean isAvailable ;

    private String displayName;

    private String bio;

    private String businessName;

    private BusinessType businessType;

    private String establishedAt;

    private String websiteUrl;

    private String telegram;

    private String facebookUrl;

    private Instant verifiedAt;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant deletedAt;

    private ProviderStatus status;
}
