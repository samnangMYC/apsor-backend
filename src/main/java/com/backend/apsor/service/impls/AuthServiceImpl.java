package com.backend.apsor.service.impls;

import com.backend.apsor.entities.Users;
import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.exceptions.ApiException;
import com.backend.apsor.payloads.requests.SignInReq;
import com.backend.apsor.payloads.response.AuthResponse;
import com.backend.apsor.payloads.response.TokenResponse;
import com.backend.apsor.repositories.UserRepo;
import com.backend.apsor.service.auth.AuthCookieService;
import com.backend.apsor.service.auth.AuthService;
import com.backend.apsor.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthCookieService cookieService;
    private final UserRepo usersRepository;
    private final JwtDecoder jwtDecoder;

    private final WebClient webClient = WebClient.builder().build();

    @Value("${keycloak.base-url}") private String baseUrl;
    @Value("${keycloak.realm}") private String realm;
    @Value("${keycloak.client-id}") private String clientId;
    @Value("${keycloak.client-secret:}") private String clientSecret;

    @Override
    public AuthResponse login(SignInReq req, HttpServletResponse response) {
        TokenResponse token = tokenByPassword(req.getUsername(), req.getPassword());
        cookieService.setAuthCookies(response, token);

        return buildAuthResponse(token,true);
    }

    @Override
    public AuthResponse refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtil.get(request, AuthCookieService.REFRESH_COOKIE);
        if (refreshToken == null || refreshToken.isBlank()) {
            throw ApiException.unauthorized(ApiErrorCode.INVALID_CREDENTIALS, "Missing refresh token.");
        }
        TokenResponse token = tokenByRefresh(refreshToken);
        cookieService.setAuthCookies(response, token);

        return buildAuthResponse(token,false);
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtil.get(request, AuthCookieService.REFRESH_COOKIE);
        // 1) clear cookies first (idempotent + fast client logout)
        cookieService.clearAuthCookies(response);

        // 2) Best-effort Keycloak logout
        if (refreshToken != null && !refreshToken.isBlank()) {
            try {
                callKeycloakLogout(refreshToken);
            } catch (Exception ex) {
                // log and ignore; user is already logged out locally
                log.warn("Keycloak logout failed (ignored). {}", ex.getMessage());
            }
        }
        return "You have been logged out successfully.";
    }

    private TokenResponse tokenByPassword(String username, String password) {
        String tokenUrl = baseUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        return webClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", clientId)
                        .with("username", username)
                        .with("password", password)
                        .with("client_secret", (clientSecret == null || clientSecret.isBlank()) ? null : clientSecret)
                )
                .exchangeToMono(res -> {
                    if (res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TokenResponse.class);
                    }
                    return res.bodyToMono(String.class).defaultIfEmpty("")
                            .flatMap(body -> {
                                // Log for debugging
                                log.error("Keycloak token error status={} body={}", res.statusCode().value(), body);

                                // Common Keycloak error: invalid_grant / not verified / required actions
                                if (body.contains("Account is not fully set up")) {
                                    return Mono.error(ApiException.forbidden(
                                            ApiErrorCode.EMAIL_NOT_VERIFIED,
                                            "Please verify your email before login."
                                    ));
                                }
                                if (body.contains("invalid_grant")) {
                                    return Mono.error(ApiException.unauthorized(
                                            ApiErrorCode.INVALID_CREDENTIALS,
                                            "Invalid username or password."
                                    ));
                                }
                                return Mono.error(ApiException.unauthorized(
                                        ApiErrorCode.INVALID_CREDENTIALS,
                                        "Login failed."
                                ));
                            });
                })
                .block();
    }

    private TokenResponse tokenByRefresh(String refreshToken) {
        String tokenUrl = baseUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("client_id", clientId);
        if (clientSecret != null && !clientSecret.isBlank()) form.add("client_secret", clientSecret);
        form.add("refresh_token", refreshToken);

        return webClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(form))
                .retrieve()
                .onStatus(s -> s.value() == 400 || s.value() == 401, r ->
                        Mono.error(ApiException.unauthorized(
                                ApiErrorCode.INVALID_CREDENTIALS,
                                "Session expired. Please login again."
                        ))
                )
                .bodyToMono(TokenResponse.class)
                .block();
    }

    private void callKeycloakLogout(String refreshToken) {
        String logoutUrl = baseUrl + "/realms/" + realm + "/protocol/openid-connect/logout";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", clientId);
        if (clientSecret != null && !clientSecret.isBlank()) form.add("client_secret", clientSecret);
        form.add("refresh_token", refreshToken);

        webClient.post()
                .uri(logoutUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(form))
                .retrieve()
                .toBodilessEntity()
                .onErrorResume(ex -> Mono.empty())
                .block();
    }

    private AuthResponse buildAuthResponse(TokenResponse token, boolean updateLoginAt) {
        Jwt jwt = jwtDecoder.decode(token.getAccessToken());

        String keycloakUserId = jwt.getSubject();
        Instant expiresAt = jwt.getExpiresAt() != null ? jwt.getExpiresAt() : Instant.now().plusSeconds(300);

        Boolean ev = jwt.getClaim("email_verified");
        boolean emailVerified = ev != null && ev;

        List<String> roles = List.of();
        Object realmAccessObj = jwt.getClaims().get("realm_access");
        if (realmAccessObj instanceof Map<?, ?> realmAccess) {
            Object rolesObj = realmAccess.get("roles");
            if (rolesObj instanceof List<?> list) {
                roles = list.stream()
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .distinct()
                        .toList();
            }
        }

        Users u = usersRepository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> ApiException.notFound(ApiErrorCode.USER_NOT_FOUND, "User not found in database."));

        Instant now = Instant.now();
        if (updateLoginAt) {
            u.setLastLoginAt(now);
        }
        u.setLastSeenAt(now);
        usersRepository.save(u);

        return AuthResponse.builder()
                .success(true)
                .message("OK")
                .username(u.getUsername())
                .email(u.getEmail())
                .userTypes(u.getUserType())
                .roles(roles)
                .expiresAt(expiresAt)
                .emailVerified(emailVerified)
                .build();
    }
}
