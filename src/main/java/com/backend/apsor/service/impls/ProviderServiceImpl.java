package com.backend.apsor.service.impls;

import com.backend.apsor.entities.Provider;
import com.backend.apsor.entities.Users;
import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.exceptions.ApiException;
import com.backend.apsor.mapper.ProviderMapper;
import com.backend.apsor.payloads.dtos.AdminProviderDTO;
import com.backend.apsor.payloads.dtos.UserDTO;
import com.backend.apsor.payloads.requests.AdminProviderReq;
import com.backend.apsor.payloads.requests.ProviderStatusReq;
import com.backend.apsor.repositories.ProviderRepo;
import com.backend.apsor.repositories.UserRepo;
import com.backend.apsor.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final UserRepo userRepo;
    private final ProviderRepo providerRepo;
    private final ProviderMapper providerMapper;

    @Override
    public AdminProviderDTO createNewProviderByAdmin(AdminProviderReq req) {
        Users user = userRepo.findById(Long.valueOf(req.getUserId()))
                .orElseThrow(() ->  ApiException.notFound(
                        ApiErrorCode.USER_NOT_FOUND,
                        "User not found with id:",
                        req.getUserId()
                ));

        // Check if the user already has a provider (prevents duplicates)
        if (providerRepo.findByUser(user).isPresent()) {
            throw ApiException.conflict(
                    ApiErrorCode.PROVIDER_ALREADY_EXISTS,
                    "A provider already exists for user with id:",
                    req.getUserId()
            );
        }

        Provider provider = providerMapper.toEntityFromAdmin(req);
        provider.setUser(user);
        provider.setAvailable(true);

        return providerMapper.toAdminDto(providerRepo.save(provider));
    }

    @Override
    public List<AdminProviderDTO> getAllProviderByAdmin() {
        return providerRepo.findAll()
                .stream()
                .map(providerMapper::toAdminDto).toList();
    }

    @Override
    public AdminProviderDTO getProviderByIdFromAdmin(Long id) {
        return providerRepo.findById(id)
                .map(providerMapper::toAdminDto)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "Provider not found with id: ",
                        id.toString()
                ));
    }

    @Override
    public AdminProviderDTO updateProviderByIdFromAdmin(Long id) {
        Provider provider = providerRepo.findById(id)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "Provider not found with id: ",
                        id.toString()
                ));

        return providerMapper.toAdminDto(providerRepo.save(provider));
    }

    @Override
    public AdminProviderDTO updateStatusByIdFromAdmin(Long id, ProviderStatusReq req) {
        return providerRepo.findById(id)
                .map(provider -> {
                    provider.setStatus(req.getStatus());
                    return providerMapper.toAdminDto(providerRepo.save(provider));
                }).orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "Provider not found with id: ",
                        id.toString()
                ));
    }

    @Override
    public String deleteProviderByIdFromAdmin(Long id) {

        return providerRepo.findById(id)
                .map(provider -> {
                    providerRepo.delete(provider);
                    return "Provider has been deleted";
                }).orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.PROVIDER_NOT_FOUND,
                        "Provider not found with id: ",
                        id.toString()
                ));
    }



}
