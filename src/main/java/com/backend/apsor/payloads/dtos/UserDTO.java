package com.backend.apsor.payloads.dtos;

import com.backend.apsor.enums.UserStatus;
import com.backend.apsor.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

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

    private String profileImageUrl;

    private Instant lastLoginAt;

    private Instant lastSeenAt;

    private boolean online;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant deletedAt;
}
