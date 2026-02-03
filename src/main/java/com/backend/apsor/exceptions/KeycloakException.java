package com.backend.apsor.exceptions;

import com.backend.apsor.enums.ApiErrorCode;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Component;

@Component
public class KeycloakException {

    public ApiException toApiException(WebApplicationException ex, String prefix) {
        Response r = ex.getResponse();
        int status = r != null ? r.getStatus() : 500;

        String body = safeReadBody(r);
        String msg = prefix
                + " | status=" + status
                + " | body=" + (body != null ? body : ex.getMessage());

        return switch (status) {
            case 400 -> ApiException.badRequest(ApiErrorCode.KEYCLOAK_BAD_REQUEST, msg);
            case 401 -> ApiException.unauthorized(ApiErrorCode.KEYCLOAK_UNAUTHORIZED, msg);
            case 403 -> ApiException.forbidden(ApiErrorCode.KEYCLOAK_FORBIDDEN, msg);
            case 404 -> ApiException.notFound(ApiErrorCode.KEYCLOAK_USER_NOT_FOUND, msg);
            case 409 -> ApiException.conflict(ApiErrorCode.KEYCLOAK_CONFLICT, msg);
            default  -> new ApiException(
                    org.springframework.http.HttpStatus.valueOf(status),
                    ApiErrorCode.KEYCLOAK_ERROR,
                    msg
            );
        };
    }

    private String safeReadBody(Response r) {
        if (r == null) return null;
        try {
            if (r.hasEntity()) return r.readEntity(String.class);
        } catch (Exception ignore) {}
        return null;
    }
}
