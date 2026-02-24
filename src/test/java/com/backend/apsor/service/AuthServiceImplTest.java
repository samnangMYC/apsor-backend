package com.backend.apsor.service;

import com.backend.apsor.entities.Users;
import com.backend.apsor.enums.UserStatus;
import com.backend.apsor.enums.UserType;
import com.backend.apsor.payloads.dtos.UserDTO;
import com.backend.apsor.payloads.requests.SignUpReq;
import com.backend.apsor.repositories.UserRepo;
import com.backend.apsor.service.impls.UserServiceImpl;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    UserRepo usersRepository;
    @Mock
    Keycloak keycloakAdmin;

    // Keycloak resource graph mocks
    @Mock
    RealmResource realm ;
    @Mock
    UsersResource usersResource;
    @Mock
    UserResource userResource;
    @Mock
    RolesResource roles;
    @Mock RoleResource roleResource;
    @Mock
    RoleRepresentation roleRepresentation;
    @Mock
    RoleMappingResource roleMapping;
    @Mock RoleScopeResource realmLevel;

    @InjectMocks
    UserServiceImpl service;

    @BeforeEach
    void setup() {
        when(keycloakAdmin.realm(anyString())).thenReturn(realm);
        when(realm.users()).thenReturn(usersResource);

        when(realm.roles()).thenReturn(roles);
        when(roles.get(anyString())).thenReturn(roleResource);
        when(roleResource.toRepresentation()).thenReturn(roleRepresentation);

        when(usersResource.get(anyString())).thenReturn(userResource);
        when(userResource.roles()).thenReturn(roleMapping);
        when(roleMapping.realmLevel()).thenReturn(realmLevel);

        // default DB checks: no duplicates
        when(usersRepository.existsByEmail(anyString())).thenReturn(false);
        when(usersRepository.existsByUsername(anyString())).thenReturn(false);
        when(usersRepository.existsByPhoneNumber(anyString())).thenReturn(false);

        // default KC email search: not found
        when(usersResource.searchByEmail(anyString(), eq(true))).thenReturn(List.of());
    }

    @Test
    void signUpCustomer_success_shouldCreateUserSetPasswordAssignRoleSaveDb_returnDto() {
        // Arrange
        SignUpReq req = sampleReq();

        // Mock Keycloak create user -> 201 and Created ID
        Response createResp = mock(Response.class);
        when(createResp.getStatus()).thenReturn(201);

        when(usersResource.create(any(UserRepresentation.class))).thenReturn(createResp);

        // Static mocking for CreatedResponseUtil.getCreatedId(...)
        try (MockedStatic<CreatedResponseUtil> mocked =
                     Mockito.mockStatic(org.keycloak.admin.client.CreatedResponseUtil.class)) {

            mocked.when(() -> org.keycloak.admin.client.CreatedResponseUtil.getCreatedId(createResp))
                    .thenReturn("kc-123");

            // Mock save in DB
            Users saved = new Users();
            saved.setId(Long.valueOf(1));
            saved.setKeycloakUserId("kc-123");
            saved.setUserType(UserType.CUSTOMER);
            saved.setStatus(UserStatus.ACTIVE);
            saved.setUsername(req.getUsername());
            saved.setEmail(req.getEmail());
            saved.setFirstName(req.getFirstName());
            saved.setLastName(req.getLastName());
            saved.setPhoneNumber(req.getPhoneNumber());
            saved.setCreatedAt(Instant.now());
            saved.setUpdatedAt(Instant.now());

            when(usersRepository.save(any(Users.class))).thenReturn(saved);

            // Act
            UserDTO dto = service.signUpCustomer(req);

            // Assert
            assertNotNull(dto);
            assertEquals("kc-123", dto.getKeycloakUserId());
            assertEquals(UserType.CUSTOMER, dto.getUserType());
            assertEquals(req.getEmail(), dto.getEmail());

            // Verify Keycloak calls
            verify(usersResource).create(any(UserRepresentation.class));
            verify(userResource).resetPassword(any(CredentialRepresentation.class));
            verify(realmLevel).add(argThat(list ->
                    list.size() == 1 && list.get(0) == roleRepresentation
            ));

            // Verify role name used is CUSTOMER
            verify(roles).get("CUSTOMER");

            // Verify DB save has correct values
            verify(usersRepository).save(argThat(u ->
                    "kc-123".equals(u.getKeycloakUserId()) &&
                            u.getUserType() == UserType.CUSTOMER &&
                            u.getStatus() == UserStatus.ACTIVE &&
                            req.getEmail().equals(u.getEmail())
            ));

            // Make sure response closed by try-with-resources
            verify(createResp).close();
        }
    }
    private SignUpReq sampleReq() {
        SignUpReq req = new SignUpReq();
        req.setUsername("user1");
        req.setEmail("a@b.com");
        req.setFirstName("A");
        req.setLastName("B");
        req.setPhoneNumber("012345678");
        req.setPassword("pass123");
        return req;
    }

}
