package com.hussam.inventory.inventory.service;

import com.hussam.inventory.inventory.dto.request.CartRequest;
import com.hussam.inventory.inventory.entities.Cart;
import com.hussam.inventory.inventory.security.services.UserDetailsImp;

public interface CartService {

    public Cart addToCart(CartRequest cartRequest, UserDetailsImp currentUser);

    void clearCart(UserDetailsImp currentUser);

    Cart exploreCart(UserDetailsImp currentUser);

    public Cart increaseCartElement(Long elemId, Integer amount, UserDetailsImp currentUser);

//    Cart decreaseCartElement(Long elemId, Integer amount, UserDetailsImpl currentUser);
//    void deleteElementFromCart(Long id, UserDetailsImpl userDetails);
}
