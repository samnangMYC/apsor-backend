package com.backend.apsor.service.auth;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeycloakAdminClient {
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public void disableUser(String keycloakUserId) {
        var userResource = keycloak.realm(realm).users().get(keycloakUserId);
        UserRepresentation rep = userResource.toRepresentation();
        rep.setEnabled(false);
        userResource.update(rep);
    }

    public void deleteUser(String keycloakUserId) {
        keycloak.realm(realm).users().delete(keycloakUserId);
    }
}
