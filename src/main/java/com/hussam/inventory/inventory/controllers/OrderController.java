package com.hussam.inventory.inventory.controllers;

import com.hussam.inventory.inventory.entities.User;
import com.hussam.inventory.inventory.entities.Order;

import com.hussam.inventory.inventory.exception.NotFoundException;
import com.hussam.inventory.inventory.security.services.CurrentUser;
import com.hussam.inventory.inventory.security.services.UserDetailsImp;
import com.hussam.inventory.inventory.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createOrder(@CurrentUser UserDetailsImp user) {
        LOGGER.info("Inside createOrder() ");
        Order newOrder = orderService.createOrder(user);

        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAll(@CurrentUser UserDetailsImp currentUser){
        LOGGER.info("Inside getAll()");
        List<Order> orderList = orderService.findALl(currentUser);
        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getOrderById(@PathVariable("id") Long orderId, @CurrentUser UserDetailsImp currentUser){
        LOGGER.info("Inside getOrderById() for"+ orderId);
        Optional<Order> selectedOrder = orderService.getOrderById(orderId, currentUser);

        if(!selectedOrder.isPresent()){
            LOGGER.error("Order with id: " + orderId + " not found");
            throw new NotFoundException("There is no order made with this id: "  +orderId);
        }

        return new ResponseEntity<>(selectedOrder, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteOrderById(@PathVariable("id") Long orderId,@CurrentUser UserDetailsImp currentUser){
        LOGGER.info("Inside deleteOrderById() for "+ orderId);

        orderService.delete(orderId, currentUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
