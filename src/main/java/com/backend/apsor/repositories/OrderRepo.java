package com.backend.apsor.repositories;

import com.backend.apsor.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long> {
}
