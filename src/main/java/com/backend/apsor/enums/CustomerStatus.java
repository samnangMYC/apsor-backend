package com.backend.apsor.enums;

public enum CustomerStatus {
    ACTIVE,     // normal
    SUSPENDED,  // cannot book / restricted by admin
    INACTIVE    // user deactivated profile (optional)
}
