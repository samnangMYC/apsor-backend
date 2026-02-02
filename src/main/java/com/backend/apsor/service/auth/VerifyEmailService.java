package com.backend.apsor.service.auth;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VerifyEmailService {
    private final Keycloak keycloakAdmin;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.public-client-id}")
    private String clientId;

    @Value("${app.frontend.verify-redirect-uri}")
    private String verifyRedirectUri;

    public void sendVerifyEmailByUserId(String keycloakUserId) {
        RealmResource realmResource = keycloakAdmin.realm(realm);
        UserResource userResource = realmResource.users().get(keycloakUserId);

        UserRepresentation rep = userResource.toRepresentation();
        if (Boolean.TRUE.equals(rep.isEmailVerified())) return;

        // Send VERIFY_EMAIL and redirect to React after success
        userResource.executeActionsEmail(
                verifyRedirectUri,
                clientId,
                List.of("VERIFY_EMAIL")
        );
    }

    public void resendForCurrentUser(Jwt jwt) {
        sendVerifyEmailByUserId(jwt.getSubject());
    }

    public boolean isEmailVerified(Jwt jwt) {
        Boolean claim = jwt.getClaim("email_verified");
        if (claim != null) return claim;

        UserRepresentation rep = keycloakAdmin.realm(realm)
                .users()
                .get(jwt.getSubject())
                .toRepresentation();

        return Boolean.TRUE.equals(rep.isEmailVerified());
    }
}
