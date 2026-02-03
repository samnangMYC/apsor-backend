package com.backend.apsor.service.impls;

import com.backend.apsor.entities.Users;
import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.enums.UserStatus;
import com.backend.apsor.enums.UserType;
import com.backend.apsor.exceptions.ApiException;
import com.backend.apsor.exceptions.KeycloakException;
import com.backend.apsor.mapper.UserMapper;
import com.backend.apsor.payloads.dtos.UserDTO;
import com.backend.apsor.payloads.requests.CreateUserByAdminReq;
import com.backend.apsor.payloads.requests.SignUpReq;
import com.backend.apsor.payloads.requests.UpdateMeReq;
import com.backend.apsor.payloads.requests.UpdateUserByAdminReq;
import com.backend.apsor.repositories.UserRepo;
import com.backend.apsor.service.UserService;
import com.backend.apsor.service.auth.KeycloakAdminClient;
import com.backend.apsor.service.auth.VerifyEmailService;
import jakarta.ws.rs.WebApplicationException;
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
    private final KeycloakAdminClient keycloakAdminClient;
    private final UserMapper userMapper;
    private final KeycloakException keycloakError;

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
           // verifyEmailService.sendVerifyEmailByUserId(keycloakUserId);

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
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Override
    public UserDTO getUserByIdFromAdmin(Long id) {
        Users users = usersRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound(ApiErrorCode.USER_NOT_FOUND, "User not found: " + id));

        return userMapper.toDTO(users);
    }

    @Override
    public List<UserDTO> getAllUserFromAdmin() {
        return usersRepository.findAll()
                .stream()
                .map(userMapper::toDTO).toList();
    }

    @Override
    @Transactional
    public UserDTO createUserByAdmin(CreateUserByAdminReq req) {

        // DB uniqueness checks
        if (usersRepository.existsByEmail(req.getEmail())) {
            throw ApiException.conflict(ApiErrorCode.EMAIL_ALREADY_EXISTS,
                    "Email already exists: %s", req.getEmail());
        }
        if (usersRepository.existsByUsername(req.getUsername())) {
            throw ApiException.conflict(ApiErrorCode.USERNAME_ALREADY_EXISTS,
                    "Username already exists: %s", req.getUsername());
        }

        RealmResource realm = keycloakAdmin.realm(REALM);

        // Keycloak uniqueness checks (email at least)
        if (!realm.users().searchByEmail(req.getEmail(), true).isEmpty()) {
            throw ApiException.conflict(ApiErrorCode.EMAIL_ALREADY_EXISTS,
                    "Email already exists in Keycloak: %s", req.getEmail());
        }

        // Map userType -> realm role name
        String roleName = switch (req.getUserType()) {
            case CUSTOMER -> "CUSTOMER";
            case PROVIDER -> "PROVIDER";
            case ADMIN -> "ADMIN";
        };

        // Create user in Keycloak
        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(req.getUsername());
        kcUser.setEmail(req.getEmail());
        kcUser.setFirstName(req.getFirstName());
        kcUser.setLastName(req.getLastName());
        kcUser.setEmailVerified(false);
        kcUser.setEnabled(true);

        String keycloakUserId;
        try (Response resp = realm.users().create(kcUser)) {
            if (resp.getStatus() != 201) {
                String body = resp.readEntity(String.class);
                log.error("Keycloak create user failed status={} body={}", resp.getStatus(), body);
                throw ApiException.badRequest(ApiErrorCode.KEYCLOAK_CREDENTIAL_ERROR,
                        "Failed to create user in Keycloak");
            }
            keycloakUserId = CreatedResponseUtil.getCreatedId(resp);
        }

        try {
            UserResource userResource = realm.users().get(keycloakUserId);

            // OPTIONAL: set temporary password if your request includes it
            // If you don't want admin to set passwords, remove this block and instead send UPDATE_PASSWORD action email.
            if (req.getTemporaryPassword() != null && !req.getTemporaryPassword().isBlank()) {
                CredentialRepresentation pwd = new CredentialRepresentation();
                pwd.setType(CredentialRepresentation.PASSWORD);
                pwd.setTemporary(true); // force change on first login (recommended for admin-created users)
                pwd.setValue(req.getTemporaryPassword());
                userResource.resetPassword(pwd);
            } else {
                // Best practice: require user to set password via email action (Keycloak)
                // Uncomment if your SMTP + redirect is configured:
                // userResource.executeActionsEmail(List.of("UPDATE_PASSWORD", "VERIFY_EMAIL"));
            }

            // Assign realm role
            RoleRepresentation role = realm.roles().get(roleName).toRepresentation();
            userResource.roles().realmLevel().add(List.of(role));

            // Save in DB
            Users u = new Users();
            u.setKeycloakUserId(keycloakUserId);
            u.setUsername(req.getUsername());
            u.setEmail(req.getEmail());
            u.setFirstName(req.getFirstName());
            u.setLastName(req.getLastName());
            u.setPhoneNumber(req.getPhoneNumber());
            u.setUserType(req.getUserType());
            u.setStatus(UserStatus.ACTIVE);

            Users saved = usersRepository.save(u);

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
                    .createdAt(saved.getCreatedAt())
                    .updatedAt(saved.getUpdatedAt())
                    .build();

        } catch (Exception ex) {
            // Rollback Keycloak user if anything after creation fails
            try {
                realm.users().get(keycloakUserId).remove();
                log.warn("Rolled back Keycloak user {}", keycloakUserId);
            } catch (Exception rollbackEx) {
                log.error("Rollback failed for Keycloak user {}", keycloakUserId, rollbackEx);
            }
            throw ex;
        }
    }

    @Override
    @Transactional
    public UserDTO updateUserByAdmin(Long id, UpdateUserByAdminReq req) {

        Users u = usersRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound(ApiErrorCode.USER_NOT_FOUND, "User not found"));

        // ---- basic safety
        String kcId = u.getKeycloakUserId();
        if (kcId == null || kcId.isBlank()) {
            throw ApiException.badRequest(ApiErrorCode.KEYCLOAK_ID_MISSING,
                    "User has no keycloakUserId (userId=%s)", id);
        }

        UserType oldType = u.getUserType();
        UserStatus oldStatus = u.getStatus();
        String oldEmail = u.getEmail();
        String oldUsername = u.getUsername();

        // normalize
        String newUsername = req.getUsername() != null ? req.getUsername().trim() : null;
        String newEmail = req.getEmail() != null ? req.getEmail().trim() : null;

        // ---- Unique checks (DB)
        boolean email = newEmail != null && !newEmail.isBlank() && (!newEmail.equalsIgnoreCase(oldEmail));
        if (email) {
            if (usersRepository.existsByEmail(newEmail)) {
                throw ApiException.conflict(ApiErrorCode.EMAIL_ALREADY_EXISTS,
                        "Email already exists: %s", newEmail);
            }
        }

        if (newUsername != null && !newUsername.isBlank() && (!newUsername.equalsIgnoreCase(oldUsername))) {
            if (usersRepository.existsByUsername(newUsername)) {
                throw ApiException.conflict(ApiErrorCode.USERNAME_ALREADY_EXISTS,
                        "Username already exists: %s", newUsername);
            }
        }

        boolean typeChanged = req.getUserType() != null && req.getUserType() != oldType;
        boolean statusChanged = req.getStatus() != null && req.getStatus() != oldStatus;

        // ---- Update DB (only if provided)
        if (newUsername != null && !newUsername.isBlank()) u.setUsername(newUsername);
        if (newEmail != null && !newEmail.isBlank()) u.setEmail(newEmail);

        if (req.getFirstName() != null) u.setFirstName(req.getFirstName().trim());
        if (req.getLastName() != null) u.setLastName(req.getLastName().trim());
        if (req.getPhoneNumber() != null) u.setPhoneNumber(req.getPhoneNumber().trim());

        if (typeChanged) u.setUserType(req.getUserType());
        if (statusChanged) u.setStatus(req.getStatus());

        Users saved = usersRepository.save(u);

        // ---- Sync Keycloak
        RealmResource realm = keycloakAdmin.realm(REALM);
        UserResource userResource = realm.users().get(kcId);

        // Start from current rep (safer than creating new)
        UserRepresentation rep = userResource.toRepresentation();

        // IMPORTANT: only set fields when request gives non-blank values
        if (newUsername != null && !newUsername.isBlank()) rep.setUsername(saved.getUsername());
        if (newEmail != null && !newEmail.isBlank()) rep.setEmail(saved.getEmail());
        if (req.getFirstName() != null) rep.setFirstName(saved.getFirstName());
        if (req.getLastName() != null) rep.setLastName(saved.getLastName());

        if (email) rep.setEmailVerified(false);

        if (statusChanged) rep.setEnabled(saved.getStatus() == UserStatus.ACTIVE);

        // Keycloak update with readable error body
        try {
            userResource.update(rep);
        } catch (WebApplicationException ex) {
            throw keycloakError.toApiException(ex, "Keycloak rejected user update (kcId=" + kcId + ")");
        }

        // ---- Update password (Keycloak) if provided
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            CredentialRepresentation cred = new CredentialRepresentation();
            cred.setType(CredentialRepresentation.PASSWORD);
            cred.setValue(req.getPassword());
            cred.setTemporary(false);

            try {
                userResource.resetPassword(cred);
            } catch (WebApplicationException ex) {
                throw keycloakError.toApiException(ex, "Keycloak rejected password reset (kcId=" + kcId + ")");
            }
        }

        // ---- Sync roles if type changed
        if (typeChanged) {
            String oldRole = oldType.name();
            String newRole = saved.getUserType().name();

            RoleRepresentation oldR = realm.roles().get(oldRole).toRepresentation();
            RoleRepresentation newR = realm.roles().get(newRole).toRepresentation();

            try {
                userResource.roles().realmLevel().remove(List.of(oldR));
                userResource.roles().realmLevel().add(List.of(newR));
            } catch (WebApplicationException ex) {
                throw keycloakError.toApiException(ex, "Keycloak rejected role update (kcId=" + kcId + ")");
            }
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
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    // -------------------------
    // Delete (soft delete + disable Keycloak)
    // -------------------------
    @Override
    public String softDeleteUserByAdmin(Long id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound(ApiErrorCode.USER_NOT_FOUND, "User not found: " + id));

        // already soft deleted -> idempotent
        if (user.getDeletedAt() != null) {
            return "User already soft-deleted";
        }

        String keycloakUserId = user.getKeycloakUserId();
        if (keycloakUserId == null || keycloakUserId.isBlank()) {
            throw ApiException.badRequest(ApiErrorCode.KEYCLOAK_ID_MISSING, "User has no keycloakUserId: " + id);
        }

        // 1) Disable in Keycloak (prevents login, keeps audit)
        try {
            keycloakAdminClient.disableUser(keycloakUserId);
        } catch (Exception ex) {
            // If Keycloak user is missing, you can choose:
            // - treat as not found (strict)
            // - or ignore and still soft delete locally (lenient)
            throw ApiException.notFound(ApiErrorCode.KEYCLOAK_USER_NOT_FOUND, "Keycloak user not found: " + keycloakUserId);
        }

        // 2) Soft delete locally
        user.setDeletedAt(Instant.now());
        user.setStatus(UserStatus.DELETED);
        usersRepository.save(user);

        log.info("Soft deleted user. localId={}, keycloakUserId={}", id, keycloakUserId);
        return "User soft deleted successfully";
    }

    @Override
    public String hardDeleteUserByAdmin(Long id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound(ApiErrorCode.USER_NOT_FOUND, "User not found: " + id));

        String keycloakUserId = user.getKeycloakUserId();
        if (keycloakUserId == null || keycloakUserId.isBlank()) {
            throw ApiException.badRequest(ApiErrorCode.KEYCLOAK_ID_MISSING, "User has no keycloakUserId: " + id);
        }

        // 1) Delete from Keycloak permanently (best-effort or strict)
        try {
            keycloakAdminClient.deleteUser(keycloakUserId);
        } catch (Exception ex) {
            // recommended: idempotent hard delete:
            // if Keycloak user already deleted -> continue
            log.warn("Keycloak hard delete failed/ignored. keycloakUserId={}, reason={}", keycloakUserId, ex.getMessage());
        }

        // 2) Hard delete locally
        usersRepository.delete(user);

        log.info("Hard deleted user. localId={}, keycloakUserId={}", id, keycloakUserId);
        return "User hard deleted successfully";
        }


        @Override
        public Users loadUserByJwt(Jwt jwt) {
        String kcUserId = jwt.getSubject();

        return usersRepository.findByKeycloakUserId(kcUserId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.USER_NOT_FOUND,
                        "User not found with id: ",
                        kcUserId
                ));

    }

}
