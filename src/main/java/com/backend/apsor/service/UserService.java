package com.backend.apsor.service;

import com.backend.apsor.payloads.requests.SignUpReq;
import jakarta.validation.Valid;

public interface UserService {

    String registerNewUser(@Valid SignUpReq req);
}
