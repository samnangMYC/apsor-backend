package com.backend.apsor.service;

import com.backend.apsor.entities.Users;
import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.enums.UserStatus;
import com.backend.apsor.enums.UserType;
import com.backend.apsor.exceptions.ApiException;
import com.backend.apsor.payloads.dtos.UserDTO;
import com.backend.apsor.payloads.requests.AdminUpdateUserReq;
import com.backend.apsor.payloads.requests.SignUpReq;
import com.backend.apsor.payloads.requests.UpdateMeReq;
import com.backend.apsor.repositories.UserRepo;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Keycloak keycloakAdmin;
    private final UserRepo usersRepository;
    private final VerifyEmailService verifyEmailService;

    @Value("${keycloak.realm}")
    private String REALM;

    // -------------------------
    // Signup (public)
    // -------------------------
    @Override
    @Transactional
    public UserDTO signUpCustomer(SignUpReq req) {
        return signup(req, UserType.CUSTOMER);
    }

    @Override
    @Transactional
    public UserDTO signUpProvider(SignUpReq req) {
        return signup(req, UserType.PROVIDER);
    }

    // Admin creation (admin-only endpoint should call this)
    @Override
    @Transactional
    public UserDTO createAdmin(SignUpReq req) {
        return signup(req, UserType.ADMIN);
    }

    private UserDTO signup(SignUpReq req, UserType type) {
        // Local DB fast checks
        if (usersRepository.existsByEmail(req.getEmail())) {
            throw ApiException.conflict(
                    ApiErrorCode.EMAIL_ALREADY_EXISTS,
                    "Email already exists",
                    req.getEmail());
        }
        if (usersRepository.existsByUsername(req.getUsername())) {
            throw ApiException.conflict(
                    ApiErrorCode.USERNAME_ALREADY_EXISTS,
                    "Username already exists",
                    req.getUsername());
        }

        RealmResource realm = keycloakAdmin.realm(REALM);

        // Keycloak check
        if (!realm.users().searchByEmail(req.getEmail(), true).isEmpty()) {
            throw ApiException.conflict(
                    ApiErrorCode.EMAIL_ALREADY_EXISTS,
                    "Email already exists",
                    req.getEmail());
        }

        String roleName = switch (type) {
            case CUSTOMER -> "CUSTOMER";
            case PROVIDER -> "PROVIDER";
            case ADMIN -> "ADMIN";
        };

        // 1) Create user in Keycloak
        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(req.getUsername());
        kcUser.setEmail(req.getEmail());
        kcUser.setFirstName(req.getFirstName());
        kcUser.setLastName(req.getLastName());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        String keycloakUserId;
        try (Response resp = realm.users().create(kcUser)) {
            if (resp.getStatus() != 201) {
                String body = resp.readEntity(String.class);
                log.error("Keycloak create user failed status={} body={}", resp.getStatus(), body);
                throw ApiException.badRequest(
                        ApiErrorCode.KEYCLOAK_CREDENTIAL_ERROR
                        ,"Failed to create user in Keycloak"
                );
            }
            keycloakUserId = CreatedResponseUtil.getCreatedId(resp);
        }

        try {
            // 2) Set password
            UserResource userResource = realm.users().get(keycloakUserId);

            CredentialRepresentation password = new CredentialRepresentation();
            password.setType(CredentialRepresentation.PASSWORD);
            password.setTemporary(false);
            password.setValue(req.getPassword());
            userResource.resetPassword(password);

            // 3) Assign realm role
            RoleRepresentation role = realm.roles().get(roleName).toRepresentation();
            userResource.roles().realmLevel().add(List.of(role));

            // 4) Save in DB
            Users u = new Users();
            u.setKeycloakUserId(keycloakUserId);
            u.setUserType(type);
            u.setStatus(UserStatus.ACTIVE);
            u.setUsername(req.getUsername());
            u.setEmail(req.getEmail());
            u.setFirstName(req.getFirstName());
            u.setLastName(req.getLastName());
            u.setPhoneNumber(req.getPhoneNumber());
            u.setLastLoginAt(Instant.now());
            u.setLastSeenAt(Instant.now());

            Users saved = usersRepository.save(u);

            // 5) Send verify email
            verifyEmailService.sendVerifyEmailByUserId(keycloakUserId);

            return UserDTO.builder()
                    .id(saved.getId())
                    .keycloakUserId(saved.getKeycloakUserId())
                    .userType(saved.getUserType())
                    .status(saved.getStatus())
                    .username(saved.getUsername())
                    .email(saved.getEmail())
                    .firstName(saved.getFirstName())
                    .lastName(saved.getLastName())
                    .phoneNumber(saved.getPhoneNumber())
                    .profileImageUrl(saved.getProfileImageUrl())
                    .createdAt(saved.getCreatedAt())
                    .updatedAt(saved.getUpdatedAt())
                    .build();

        } catch (Exception ex) {
            // rollback Keycloak user if DB/role/password/email fails
            try {
                realm.users().get(keycloakUserId).remove();
                log.warn("Rolled back Keycloak user {}", keycloakUserId);
            } catch (Exception rollbackEx) {
                log.error("Rollback failed for Keycloak user {}", keycloakUserId, rollbackEx);
            }
            throw ex;
        }
    }

    // -------------------------
    // Me
    // -------------------------
    @Override
    @Transactional(readOnly = true)
    public UserDTO getMe(Jwt jwt) {
        Users u = usersRepository.findByKeycloakUserId(jwt.getSubject())
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.USER_NOT_FOUND,
                        "User not found"));

        return UserDTO.builder()
                .id(u.getId())
                .keycloakUserId(u.getKeycloakUserId())
                .userType(u.getUserType())
                .status(u.getStatus())
                .username(u.getUsername())
                .email(u.getEmail())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .phoneNumber(u.getPhoneNumber())
                .profileImageUrl(u.getProfileImageUrl())
                .createdAt(u.getCreatedAt())
                .updatedAt(u.getUpdatedAt())
                .build();
    }

    // -------------------------
    // Self update (safe fields only)
    // -------------------------
    @Override
    @Transactional
    public UserDTO updateMe(Jwt jwt, UpdateMeReq req) {
        Users u = usersRepository.findByKeycloakUserId(jwt.getSubject())
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.USER_NOT_FOUND,
                        "User not found"));

        boolean emailChanged = false;

        if (req.getFirstName() != null) u.setFirstName(req.getFirstName());
        if (req.getLastName() != null) u.setLastName(req.getLastName());
        if (req.getPhoneNumber() != null) u.setPhoneNumber(req.getPhoneNumber());
        if (req.getProfileImageUrl() != null) u.setProfileImageUrl(req.getProfileImageUrl());

        if (req.getEmail() != null && !Objects.equals(req.getEmail(), u.getEmail())) {
            if (usersRepository.existsByEmail(req.getEmail())) {
                throw ApiException.conflict(
                        ApiErrorCode.EMAIL_ALREADY_EXISTS,
                        "Email already exists",
                        req.getEmail());
            }
            u.setEmail(req.getEmail());
            emailChanged = true;
        }

        u.setLastSeenAt(Instant.now());
        Users saved = usersRepository.save(u);

        // Sync Keycloak profile (minimal)
        RealmResource realm = keycloakAdmin.realm(REALM);
        UserResource ur = realm.users().get(saved.getKeycloakUserId());
        UserRepresentation rep = ur.toRepresentation();
        rep.setFirstName(saved.getFirstName());
        rep.setLastName(saved.getLastName());
        rep.setEmail(saved.getEmail());
        if (emailChanged) rep.setEmailVerified(false);
        ur.update(rep);

        if (emailChanged) {
            verifyEmailService.sendVerifyEmailByUserId(saved.getKeycloakUserId());
        }

        return UserDTO.builder()
                .id(saved.getId())
                .keycloakUserId(saved.getKeycloakUserId())
                .userType(saved.getUserType())
                .status(saved.getStatus())
                .username(saved.getUsername())
                .email(saved.getEmail())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .phoneNumber(saved.getPhoneNumber())
                .profileImageUrl(saved.getProfileImageUrl())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    // -------------------------
    // Admin update (type/status/profile)
    // -------------------------
    @Override
    @Transactional
    public UserDTO adminUpdate(Long id, AdminUpdateUserReq req) {
        Users u = usersRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.USER_NOT_FOUND,
                        "User not found"));

        UserType oldType = u.getUserType();
        boolean emailChanged = false;

        if (req.getFirstName() != null) u.setFirstName(req.getFirstName());
        if (req.getLastName() != null) u.setLastName(req.getLastName());
        if (req.getPhoneNumber() != null) u.setPhoneNumber(req.getPhoneNumber());
        if (req.getProfileImageUrl() != null) u.setProfileImageUrl(req.getProfileImageUrl());

        if (req.getEmail() != null && !Objects.equals(req.getEmail(), u.getEmail())) {
            if (usersRepository.existsByEmail(req.getEmail())) {
                throw ApiException.conflict(
                        ApiErrorCode.EMAIL_ALREADY_EXISTS,
                        "Email already exists",
                        req.getEmail());
            }
            u.setEmail(req.getEmail());
            emailChanged = true;
        }

        if (req.getStatus() != null) u.setStatus(req.getStatus());
        if (req.getUserType() != null) u.setUserType(req.getUserType());

        Users saved = usersRepository.save(u);

        RealmResource realm = keycloakAdmin.realm(REALM);
        UserResource ur = realm.users().get(saved.getKeycloakUserId());
        UserRepresentation rep = ur.toRepresentation();

        // Sync profile
        rep.setFirstName(saved.getFirstName());
        rep.setLastName(saved.getLastName());
        rep.setEmail(saved.getEmail());
        if (emailChanged) rep.setEmailVerified(false);

        // Disable login if not ACTIVE
        rep.setEnabled(saved.getStatus() == UserStatus.ACTIVE);
        ur.update(rep);

        // Sync role if type changed
        if (req.getUserType() != null && req.getUserType() != oldType) {
            String oldRole = switch (oldType) {
                case CUSTOMER -> "CUSTOMER";
                case PROVIDER -> "PROVIDER";
                case ADMIN -> "ADMIN";
            };
            String newRole = switch (saved.getUserType()) {
                case CUSTOMER -> "CUSTOMER";
                case PROVIDER -> "PROVIDER";
                case ADMIN -> "ADMIN";
            };

            RoleRepresentation oldR = realm.roles().get(oldRole).toRepresentation();
            RoleRepresentation newR = realm.roles().get(newRole).toRepresentation();
            ur.roles().realmLevel().remove(List.of(oldR));
            ur.roles().realmLevel().add(List.of(newR));
        }

        if (emailChanged) {
            verifyEmailService.sendVerifyEmailByUserId(saved.getKeycloakUserId());
        }

        return UserDTO.builder()
                .id(saved.getId())
                .keycloakUserId(saved.getKeycloakUserId())
                .userType(saved.getUserType())
                .status(saved.getStatus())
                .username(saved.getUsername())
                .email(saved.getEmail())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .phoneNumber(saved.getPhoneNumber())
                .profileImageUrl(saved.getProfileImageUrl())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    // -------------------------
    // Delete (soft delete + disable Keycloak)
    // -------------------------
    @Override
    @Transactional
    public void deleteUser(Long id) {
        Users u = usersRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.USER_NOT_FOUND,
                        "User not found"
                ));

        if (u.getDeletedAt() != null) return;

        u.setStatus(UserStatus.DELETED);
        u.setDeletedAt(Instant.now());
        usersRepository.save(u);

        RealmResource realm = keycloakAdmin.realm(REALM);
        UserResource ur = realm.users().get(u.getKeycloakUserId());
        UserRepresentation rep = ur.toRepresentation();
        rep.setEnabled(false);
        ur.update(rep);
    }
}
