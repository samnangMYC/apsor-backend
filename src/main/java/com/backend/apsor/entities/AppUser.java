package com.backend.apsor.entities;

import com.backend.apsor.enums.UserStatus;
import com.backend.apsor.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "app_users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_app_users_keycloak_user_id", columnNames = "keycloak_user_id"),
                @UniqueConstraint(name = "uk_app_users_email", columnNames = "email")
        })
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="keycloak_user_id", nullable = false, unique = true)
    private String keycloakUserId;

    // Cached profile
    private String username;
    private String email;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    // Preferences
    private String phoneNumber;
    private String profileImageUrl;

    // Activity
    private Instant lastLoginAt;
    private Instant lastSeenAt;
    private boolean isOnline;

    // Lifecycle
    private boolean onboardingCompleted = false;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    private Instant deletedAt;

}
