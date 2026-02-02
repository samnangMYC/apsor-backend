package com.backend.apsor.service.auth;

import com.backend.apsor.payloads.response.TokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class AuthCookieService {
    public static final String ACCESS_COOKIE = "ACCESS_TOKEN";
    public static final String REFRESH_COOKIE = "REFRESH_TOKEN";

    @Value("${app.auth.cookie-secure:false}")
    private boolean secure;

    @Value("${app.auth.cookie-same-site:Lax}")
    private String sameSite;

    @Value("${app.auth.cookie-domain:}")
    private String domain;

    public void setAuthCookies(HttpServletResponse response, TokenResponse token) {
        ResponseCookie access = base(ACCESS_COOKIE, token.getAccessToken())
                .path("/")
                .maxAge(token.getExpiresIn())
                .build();

        ResponseCookie refresh = base(REFRESH_COOKIE, token.getRefreshToken())
                .path("/api/v1/auth/refresh")
                .maxAge(token.getRefreshExpiresIn())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, access.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refresh.toString());
    }

    public void clearAuthCookies(HttpServletResponse response) {
        ResponseCookie access = base(ACCESS_COOKIE, "").path("/").maxAge(0).build();
        ResponseCookie refresh = base(REFRESH_COOKIE, "").path("/api/v1/auth/refresh").maxAge(0).build();
        response.addHeader(HttpHeaders.SET_COOKIE, access.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refresh.toString());
    }

    private ResponseCookie.ResponseCookieBuilder base(String name, String value) {
        var b = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite);

        if (domain != null && !domain.isBlank()) b.domain(domain);
        return b;
    }
}
