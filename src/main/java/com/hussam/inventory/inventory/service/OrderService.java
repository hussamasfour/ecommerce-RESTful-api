package com.hussam.inventory.inventory.service;


import com.hussam.inventory.inventory.entities.Order;
import com.hussam.inventory.inventory.security.services.UserDetailsImp;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    public Optional<List<Order>> getAllOrdersByUser(UserDetailsImp userDetails);

    public void delete(Long id, UserDetailsImp currentUs);

//    List<Sale> getAllSales(User user);

    double calcSaleTax(double subTotal);

    Order createOrder(UserDetailsImp user);

    Optional<Order> getOrderById(Long id, UserDetailsImp user);

    List<Order> findALl(UserDetailsImp currentUser);
}
