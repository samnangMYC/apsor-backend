package com.backend.apsor.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiErrorCode {
    // Auth
    INVALID_CREDENTIALS("AUTH_001"),
    EMAIL_NOT_VERIFIED("AUTH_002"),
    ACCESS_DENIED("AUTH_003"),

    // keycloak
    KEYCLOAK_ID_MISSING("AUTH_004"),
    KEYCLOAK_USER_NOT_FOUND("AUTH_005"),
    KEYCLOAK_CREDENTIAL_ERROR("AUTH_006"),


    // User
    EMAIL_ALREADY_EXISTS("USER_001"),
    USERNAME_ALREADY_EXISTS("USER_002"),
    USER_NOT_FOUND("USER_003"),
    ROLE_NOT_FOUND("USER_004"),


    // Validation / request
    VALIDATION_ERROR("REQ_001"),
    BAD_REQUEST("REQ_002"),

    CATEGORY_NAME_EXISTS("CAT_001"),
    CATEGORY_NOT_FOUND("CAT_002"),

    // System
    INTERNAL_ERROR("SYS_001");

    private final String code;

}
