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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImp implements OrderService {

    private static final Logger  LOGGER = LoggerFactory.getLogger(OrderServiceImp.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Override
    public Optional<List<Order>> getAllOrdersByUser(UserDetailsImp currentUser) {
        LOGGER.info("Getting logged in user info");
        User user  = userService.getUserById(currentUser.getId());

        LOGGER.info("Getting orders for user");
        return orderRepository.findByUser(user);
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
    @Transactional
    public Order createOrder(UserDetailsImp currentUser) {
        if(currentUser == null){
            throw  new NotFoundException("Please sign in first");
        }

        LOGGER.info("Fetching logged in user" );
        User user = userService.getUserById(currentUser.getId());
        Cart cart = user.getCart();

        if(cart == null){
            LOGGER.error("Error: The cart is empty");
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
                throw new InvalidArgumentException("the requested quantity for "+ cartElement.getProduct().getName() +" is bigger than what in stock");
            }

            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setProduct(cartElement.getProduct());
            orderDetails.setQuantity(cartElement.getQuantity());
            orderDetails.setOrder(newOrder);
            newOrder.getOrderDetailsList().add(orderDetails);

            product.setAmount(product.getAmount()-cartElement.getQuantity());
            LOGGER.info("Updating products quantity in the database");
            productService.update(product);
        }
        LOGGER.info("Clearing the cart");
        cartService.clearCart(currentUser);
        LOGGER.info("Saving the new order to the database");
        return orderRepository.save(newOrder);

    }

    @Override
    public Optional<Order> getOrderById(Long id, UserDetailsImp currentUser) {
        LOGGER.info("Fetching logged in user");
        User user  = userService.getUserById(currentUser.getId());
        LOGGER.info("Getting order for id: "+ id);
        return orderRepository.findOrderByIdAndAndUser(id, user);
    }

    @Override
    public List<Order> findALl(UserDetailsImp currentUser) {
        return orderRepository.findAllByOrderByDateDesc();
    }


}
