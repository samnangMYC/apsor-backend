package com.backend.apsor.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "APSOR API",
                version = "v1",
                description = "Authentication: cookies for browser + Bearer JWT for Swagger testing."
        ),
        // Default: protect endpoints unless you override per-operation
        security = { @SecurityRequirement(name = "bearerAuth") }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
// OPTIONAL: document your cookie auth (useful for documentation; Swagger may not be able to read HttpOnly)
// Cookie auth spec is apiKey in cookie. :contentReference[oaicite:6]{index=6}
@SecurityScheme(
        name = "cookieAuth",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.COOKIE,
        paramName = "ACCESS_TOKEN"
)
public class OpenApiConfig {
}
