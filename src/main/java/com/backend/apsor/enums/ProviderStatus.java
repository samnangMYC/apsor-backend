package com.backend.apsor.enums;

public enum ProviderStatus {
    DRAFT,                 // created profile but not submitted
    PENDING_VERIFICATION,  // submitted, waiting for review
    ACTIVE,                // can publish services + receive bookings
    REJECTED,              // verification failed (can resubmit)
    SUSPENDED,             // blocked by admin
    INACTIVE               // provider paused business
}
