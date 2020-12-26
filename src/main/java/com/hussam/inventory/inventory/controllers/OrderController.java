package com.hussam.inventory.inventory.controllers;

import com.hussam.inventory.inventory.entities.User;
import com.hussam.inventory.inventory.entities.Order;

import com.hussam.inventory.inventory.exception.NotFoundException;
import com.hussam.inventory.inventory.security.services.CurrentUser;
import com.hussam.inventory.inventory.security.services.UserDetailsImp;
import com.hussam.inventory.inventory.service.*;
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

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createOrder(@CurrentUser UserDetailsImp user) {
        Order newOrder = orderService.createOrder(user);

        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAll(@CurrentUser UserDetailsImp currentUser){
        List<Order> orderList = orderService.findALl(currentUser);
        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getOrderById(@PathVariable("id") Long orderId, @CurrentUser UserDetailsImp currentUser){
        Optional<Order> selectedOrder = orderService.getOrderById(orderId, currentUser);

        if(!selectedOrder.isPresent()){
            throw new NotFoundException("There is no order made with this id: "  +orderId);
        }

        return new ResponseEntity<>(selectedOrder, HttpStatus.OK);
    }

    @DeleteMapping("/{sId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteOrderById(@PathVariable("sId") Long saleId,@CurrentUser UserDetailsImp currentUser){
        User user = userService.getUserById(currentUser.getId());

        orderService.delete(saleId, currentUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
