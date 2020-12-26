package com.hussam.inventory.inventory.repositories;

import com.hussam.inventory.inventory.entities.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailsRepository  extends JpaRepository<OrderDetails, Long> {
}
