package com.hussam.inventory.inventory.service.impl;

import com.hussam.inventory.inventory.entities.*;

import com.hussam.inventory.inventory.exception.InvalidArgumentException;
import com.hussam.inventory.inventory.exception.NotFoundException;
import com.hussam.inventory.inventory.repositories.OrderRepository;
import com.hussam.inventory.inventory.repositories.UserRepository;
import com.hussam.inventory.inventory.security.services.UserDetailsImp;
import com.hussam.inventory.inventory.service.CartService;
import com.hussam.inventory.inventory.service.OrderService;
import com.hussam.inventory.inventory.service.ProductService;
import com.hussam.inventory.inventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Override
    public Optional<List<Order>> getById(UserDetailsImp user) {
        User user1  = userRepository.findByEmail(user.getEmail()).get();

        return orderRepository.findByUser(user1);

    }


    @Override
    public void delete(Long id, UserDetailsImp currentUser) {


        orderRepository.deleteById(id);
    }

    @Override
    public double calcSaleTax(double subTotal) {
        return subTotal * 0.0883;
    }

    @Override
    public Order createOrder(UserDetailsImp currentUser) {
        if(currentUser == null){
            throw  new NotFoundException("Please sign in first");
        }

        User user = userService.getUserById(currentUser.getId());
        Cart cart = user.getCart();

        if(cart == null){
            throw new NotFoundException("Error: Your cart is empty");
        }

        Order newOrder = new Order();

        newOrder.setDate(new Date());
        newOrder.setSubTotal(cart.getTotalPrice());
        newOrder.setUser(user);
        newOrder.setTax(calcSaleTax(cart.getTotalPrice()));
        newOrder.setTotal(newOrder.getSubTotal() + newOrder.getTax());

        newOrder.setOrderDetailsList(new ArrayList<>());

        List<CartElement> cartElementList  = cart.getCartElementList();
        for(CartElement cartElement: cartElementList){
            Product product  = cartElement.getProduct();

            if(cartElement.getQuantity() > product.getAmount()){
                throw new InvalidArgumentException("This item is out of stuck");
            }

            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setProduct(cartElement.getProduct());
            orderDetails.setQuantity(cartElement.getQuantity());
            orderDetails.setOrder(newOrder);
            newOrder.getOrderDetailsList().add(orderDetails);

            product.setAmount(product.getAmount()-cartElement.getQuantity());

            productService.update(product);
        }

        cartService.clearCart(currentUser);

        return orderRepository.save(newOrder);

    }

    @Override
    public Optional<Order> getOrderById(Long id, UserDetailsImp currentUser) {
        User user  = userService.getUserById(currentUser.getId());
        return orderRepository.findOrderByIdAndAndUser(id, user);
    }

    @Override
    public List<Order> findALl(UserDetailsImp currentUser) {
        return orderRepository.findAllByOrderByDateDesc();
    }


}
