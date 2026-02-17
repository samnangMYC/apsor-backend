package com.backend.apsor.payloads.dtos;

import com.backend.apsor.enums.UserStatus;
import com.backend.apsor.enums.UserType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@JsonPropertyOrder({
        "id",
        "keycloakUserId",
        "username",
        "email",
        "firstName",
        "lastName",
        "userType",
        "status",
        "phoneNumber",
        "lastLoginAt",
        "lastSeenAt",
        "createdAt",
        "updatedAt",
        "deletedAt"
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;

    private String keycloakUserId;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private UserType userType;

    private UserStatus status;

    private String phoneNumber;

    private Instant lastLoginAt;

    private Instant lastSeenAt;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant deletedAt;
}
