package com.hussam.inventory.inventory.controllers;

import com.hussam.inventory.inventory.dto.request.CartRequest;
import com.hussam.inventory.inventory.dto.request.DecrementCartElementRequest;
import com.hussam.inventory.inventory.dto.request.IncreaseCartElementRequest;
import com.hussam.inventory.inventory.dto.response.ResponseMessage;
import com.hussam.inventory.inventory.entities.Cart;
import com.hussam.inventory.inventory.exception.NotFoundException;
import com.hussam.inventory.inventory.security.services.CurrentUser;
import com.hussam.inventory.inventory.security.services.UserDetailsImp;
import com.hussam.inventory.inventory.service.CartService;
import com.hussam.inventory.inventory.service.impl.OrderServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class CartController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @PostMapping("/")
    public ResponseEntity<?> addToCart(@RequestBody @Valid CartRequest cartRequest, @CurrentUser UserDetailsImp currentUser){
        LOGGER.info("Inside addToCart() for" + cartRequest);
        Cart cart = cartService.addToCart(cartRequest, currentUser);

        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }

    @DeleteMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> clearCart(@CurrentUser UserDetailsImp userDetails){
        LOGGER.info("Inside clearCart()");
        cartService.clearCart(userDetails);
        return new ResponseEntity<>(new ResponseMessage("Your cart is empty now"),HttpStatus.OK);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getALlCartElements(@CurrentUser UserDetailsImp currentUser){
        LOGGER.info("Inside getALlCartElements()");

        Cart cart =cartService.exploreCart(currentUser);

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/increment")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> increaseSelectedCartElementAmount(@RequestBody @Valid IncreaseCartElementRequest increaseCartElementRequest, @CurrentUser UserDetailsImp currentUser){
        LOGGER.info("Inside increaseSelectedCartElementAmount()");

        Long cartElementId = increaseCartElementRequest.getElementId();
        Integer amount = increaseCartElementRequest.getAmount();
        Cart cart = cartService.increaseCartElement(cartElementId, amount, currentUser);

        return new ResponseEntity<>(cart,HttpStatus.OK);
    }

    @PostMapping("/decrement")
    public ResponseEntity<?> decreaseSelectedCartElementAmount(@RequestBody @Valid DecrementCartElementRequest decrementRequest, @CurrentUser UserDetailsImp loggedInUser){
        LOGGER.info("Inside decreaseSelectedCartElementAmount()");
        Long cartElementId = decrementRequest.getElementId();
        Integer amount = decrementRequest.getAmount();

        Cart cart = cartService.decreaseCartElement(cartElementId, amount, loggedInUser);

        return new ResponseEntity<>(cart,HttpStatus.OK);
    }

}
