package com.backend.apsor.repositories;

import com.backend.apsor.entities.Customer;
import com.backend.apsor.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    Boolean existsByUser_Id(Long id);

    Optional<Customer> findByUser(Users user);
}
