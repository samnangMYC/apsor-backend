package com.backend.apsor.service.auth;

import com.backend.apsor.payloads.requests.SignInReq;
import com.backend.apsor.payloads.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthResponse login(SignInReq req, HttpServletResponse response);
    AuthResponse refresh(HttpServletRequest request, HttpServletResponse response);
    String logout(HttpServletRequest request, HttpServletResponse response);
}
