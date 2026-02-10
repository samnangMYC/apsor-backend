package com.backend.apsor.service;

import com.backend.apsor.payloads.dtos.AdminProviderDTO;
import com.backend.apsor.payloads.dtos.ProviderDTO;
import com.backend.apsor.payloads.requests.AdminProviderReq;
import com.backend.apsor.payloads.requests.ProviderReq;
import com.backend.apsor.payloads.requests.ProviderStatusReq;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface ProviderService {
    AdminProviderDTO createNewProviderByAdmin(@Valid AdminProviderReq req);

    List<AdminProviderDTO> getAllProviderByAdmin();

    AdminProviderDTO getProviderByIdFromAdmin(Long id);

    AdminProviderDTO updateProviderByIdFromAdmin(Long id);

    String deleteProviderByIdFromAdmin(Long id);

    AdminProviderDTO updateStatusByIdFromAdmin(Long id, @Valid ProviderStatusReq req);

    ProviderDTO createNewProviderByJwt(Jwt jwt, ProviderReq req);

    ProviderDTO getProviderByJwt(Jwt jwt);
}
