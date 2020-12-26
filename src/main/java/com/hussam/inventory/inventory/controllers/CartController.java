package com.hussam.inventory.inventory.controllers;

import com.hussam.inventory.inventory.dto.request.CartRequest;
import com.hussam.inventory.inventory.dto.request.IncreaseCartElementRequest;
import com.hussam.inventory.inventory.dto.response.ResponseMessage;
import com.hussam.inventory.inventory.entities.Cart;
import com.hussam.inventory.inventory.security.services.CurrentUser;
import com.hussam.inventory.inventory.security.services.UserDetailsImp;
import com.hussam.inventory.inventory.service.CartService;
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

    @Autowired
    private CartService cartService;

    @PostMapping("/")
    public ResponseEntity<?> addToCart(@RequestBody @Valid CartRequest cartRequest, @CurrentUser UserDetailsImp currentUser){
        Cart cart = cartService.addToCart(cartRequest, currentUser);

        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }

    @DeleteMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> clearCart(@CurrentUser UserDetailsImp userDetails){
        cartService.clearCart(userDetails);
        return new ResponseEntity<>(new ResponseMessage("Your cart is empty now"),HttpStatus.OK);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getALl(@CurrentUser UserDetailsImp currentUser){
        Cart cart =cartService.exploreCart(currentUser);

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
    @PostMapping("/increment")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> increaseAmount(@RequestBody IncreaseCartElementRequest increaseCartElementRequest, @CurrentUser UserDetailsImp loggedInUser){

        Long cartElementId = increaseCartElementRequest.getElementId();
        Integer amount = increaseCartElementRequest.getAmount();
        Cart cart = cartService.increaseCartElement(cartElementId, amount, loggedInUser);

        return new ResponseEntity<>(cart,HttpStatus.OK);
    }
//
//    @PostMapping("/decrement")
//    public ResponseEntity<?> decreaseAmount(@RequestBody DecrementCartElementRequest decrementRequest, @CurrentUser UserDetailsImpl loggedInUser){
//
//        Long cartElementId = decrementRequest.getElementId();
//        Integer amount = decrementRequest.getAmount();
//
//        Cart cart = cartService.increaseCartElement(cartElementId, amount, loggedInUser);
//
//        return new ResponseEntity<>(cart,HttpStatus.OK);
//    }

}
