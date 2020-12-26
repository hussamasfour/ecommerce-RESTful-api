package com.hussam.inventory.inventory.service.impl;

import com.hussam.inventory.inventory.dto.request.CartRequest;
import com.hussam.inventory.inventory.entities.Cart;
import com.hussam.inventory.inventory.entities.CartElement;
import com.hussam.inventory.inventory.entities.Product;
import com.hussam.inventory.inventory.entities.User;
import com.hussam.inventory.inventory.exception.InvalidArgumentException;
import com.hussam.inventory.inventory.exception.NotFoundException;
import com.hussam.inventory.inventory.repositories.CartRepository;
import com.hussam.inventory.inventory.security.services.UserDetailsImp;
import com.hussam.inventory.inventory.service.CartService;
import com.hussam.inventory.inventory.service.ProductService;
import com.hussam.inventory.inventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImp implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Override
    public Cart addToCart(CartRequest cartRequest, UserDetailsImp currentUser) {
        if(currentUser == null){
            throw new NotFoundException("Please sign in first");
        }
        User user = userService.getUserById(currentUser.getId());

        Optional<Product> product = productService.getProductById(cartRequest.getProductId());
        if (!product.isPresent()) {
            throw new NotFoundException("Product with id " + cartRequest.getProductId() + " is not available");
        }
        if (product.get().getAmount() < cartRequest.getQuantity()) {
            throw new InvalidArgumentException("No enough product");
        }

        CartElement cartElement = new CartElement();
        cartElement.setQuantity(cartRequest.getQuantity());
        cartElement.setProduct(product.get());

        Cart cart = user.getCart();

        if (cart != null) {
            List<CartElement> cartElementList = cart.getCartElementList();
            for (CartElement cartElem : cartElementList) {
                if (cartElem.getProduct().equals(product.get())) {
                    cartElem.setQuantity(cartRequest.getQuantity() + cartElem.getQuantity());
                    cart.setCartElementList(cartElementList);
                    cart.setTotalPrice(cart.getTotalPrice() + product.get().getPrice());
                    return cartRepository.save(cart);
                }
            }
            cartElement.setCart(cart);
            cart.getCartElementList().add(cartElement);
            cart.setTotalPrice(cart.getTotalPrice() + product.get().getPrice());
            return cartRepository.save(cart);
        }

        Cart newCart = new Cart();
        newCart.setUser(user);
        cartElement.setCart(newCart);

        newCart.setCartElementList(new ArrayList<>());
        newCart.getCartElementList().add(cartElement);
        newCart.setTotalPrice(product.get().getPrice());

        return cartRepository.save(newCart);
    }

    @Override
    public Cart exploreCart(UserDetailsImp currentUser) {
        if(currentUser == null){
            throw new NotFoundException("Please sign in to check your cart");
        }
        User user = userService.getUserById(currentUser.getId());

        return user.getCart();
    }

    @Override
    public void clearCart(UserDetailsImp currentUser) {
        if(currentUser == null){
            throw new NotFoundException("Please sign in first");
        }
        User user = userService.getUserById(currentUser.getId());
        Cart cart = user.getCart();
        if(cart == null){
            throw  new InvalidArgumentException("Cart is already empty");
        }

        user.setCart(null);
        userService.updateUser(user);
    }

    @Override
    public Cart increaseCartElement(Long elementId, Integer amount, UserDetailsImp currentUser){
        if(currentUser == null){
            throw new NotFoundException("Please sign in first");
        }
        User user = userService.getUserById(currentUser.getId());

        Cart cart = user.getCart();

        if(cart == null){
            throw new NotFoundException("cart is empty");
        }

            List<CartElement> cartElementList = cart.getCartElementList();

        for (CartElement cartElement : cartElementList) {
            if (cartElement.getId().equals(elementId)){
                cartElement.setQuantity(cartElement.getQuantity() + amount);
                cart.setTotalPrice(cart.getTotalPrice() + (cartElement.getProduct().getPrice()* amount));
            }
        }


        return cartRepository.save(cart);
    }

//    @Override
//    public Cart decreaseCartElement(Long elementId, Integer amount, UserDetailsImpl currentUser) {
//        if (currentUser == null) {
//            throw new NotFoundException("Please sign in first");
//        }
//        User user = userService.getUserById(currentUser.getId());
//
//        Cart cart = user.getCart();
//
//        if (cart == null) {
//            throw new NotFoundException("cart is empty");
//        }
//
//        List<CartElement> cartElementList = cart.getCartElementList();
//
//        for (CartElement cartElement : cartElementList) {
//            if (cartElement.getId().equals(elementId)){
//                cartElement.setQuantity(cartElement.getQuantity() - amount);
//                cart.setTotalPrice(cart.getTotalPrice() + (cartElement.getProduct().getPrice()* amount));
//            }
//        }
//
//    }


}
