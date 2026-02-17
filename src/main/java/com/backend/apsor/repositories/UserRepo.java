package com.backend.apsor.repositories;

import com.backend.apsor.entities.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Users, Long> {
    boolean existsByEmail(String email);

    boolean existsByUsername( String username);

    Optional<Users> findByKeycloakUserId(String subject);

    boolean existsByPhoneNumber(String phoneNumber);
}
