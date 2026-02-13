package com.backend.apsor.repositories;

import com.backend.apsor.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepo extends JpaRepository<Location, Long> {
}
