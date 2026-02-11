package com.backend.apsor.repositories;

import com.backend.apsor.entities.Provider;
import com.backend.apsor.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderRepo extends JpaRepository<Provider, Long> {
    Optional<Provider> findByUser(Users user);

    Optional<Provider> findByUser_Id(Long id);
}
