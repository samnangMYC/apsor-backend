package com.backend.apsor.entities;

import com.backend.apsor.enums.UserStatus;
import com.backend.apsor.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Users {

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
    @Column(length = 30)
    private String phoneNumber;

    // Activity
    @Column(nullable = true)
    private Instant lastLoginAt;
    @Column(nullable = true)
    private Instant lastSeenAt;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Column(nullable = true)
    private Instant deletedAt;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private Provider provider;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private Customer customer;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLocation> locations = new ArrayList<>();


}
