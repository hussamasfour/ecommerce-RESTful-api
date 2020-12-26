package com.hussam.inventory.inventory.repositories;

import com.hussam.inventory.inventory.entities.Order;
import com.hussam.inventory.inventory.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

        Optional<List<Order>> findByUser(User user);

        Optional<Order> findOrderByIdAndAndUser(Long id, User user);

        List<Order> findAllByOrderByDateDesc();

}
