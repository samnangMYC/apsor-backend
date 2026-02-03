package com.backend.apsor.repositories;

import com.backend.apsor.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    Boolean existsByUser_Id(Long id);
}
