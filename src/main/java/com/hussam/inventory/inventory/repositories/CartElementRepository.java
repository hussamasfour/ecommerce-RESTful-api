package com.hussam.inventory.inventory.repositories;

import com.hussam.inventory.inventory.entities.CartElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartElementRepository extends JpaRepository<CartElement, Long> {
}
