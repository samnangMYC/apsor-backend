package com.backend.apsor.repositories;

import com.backend.apsor.entities.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLocationRepo extends JpaRepository<UserLocation, Long> {
    Optional<UserLocation> findByUser_Id(Long userId);
}
