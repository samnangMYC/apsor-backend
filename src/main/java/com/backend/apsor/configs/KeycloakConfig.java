package com.backend.apsor.configs;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8443")  // Your Keycloak base URL (no /auth or /realms)
                .realm("master")                     // Use master for admin-cli; change if using realm-specific admin client
                .clientId("admin-cli")
                .grantType(OAuth2Constants.PASSWORD)
                .username("admin")                   // Keycloak admin username
                .password("admin")                   // Keycloak admin password (use env vars/secrets in prod!)
                .build();
    }
}