package com.backend.apsor.payloads.dtos;

import com.backend.apsor.entities.Users;
import com.backend.apsor.enums.BusinessType;
import com.backend.apsor.enums.ProviderStatus;

import java.time.Instant;

public class AdminProviderDTO {

    private Long id;

    private Users user;

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
