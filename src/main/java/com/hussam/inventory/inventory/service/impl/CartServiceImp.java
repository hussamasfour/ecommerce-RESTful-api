package com.hussam.inventory.inventory.service.impl;

import com.hussam.inventory.inventory.controllers.CartController;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImp implements CartService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CartServiceImp.class);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Override
    @Transactional
    public Cart addToCart(CartRequest cartRequest, UserDetailsImp currentUser) {
        LOGGER.info("Getting logged in user "+ currentUser);
        User user = userService.getUserById(currentUser.getId());
        LOGGER.info("fetching product with id "+ cartRequest.getProductId());
        Optional<Product> product = productService.getProductById(cartRequest.getProductId());
        if (!product.isPresent()) {
            LOGGER.error("Product with id " + cartRequest.getProductId() + " is not available");
            throw new NotFoundException("Product with id " + cartRequest.getProductId() + " is not available");
        }
        if (product.get().getAmount() < cartRequest.getQuantity()) {
            LOGGER.error("Error: No enough quantity for selected product");
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
                    LOGGER.info("Saving the cart to the database");
                    return cartRepository.save(cart);
                }
            }
            cartElement.setCart(cart);
            cart.getCartElementList().add(cartElement);
            cart.setTotalPrice(cart.getTotalPrice() + product.get().getPrice());
            LOGGER.info("Saving the cart to the database");
            return cartRepository.save(cart);
        }

        Cart newCart = new Cart();
        newCart.setUser(user);
        cartElement.setCart(newCart);

        newCart.setCartElementList(new ArrayList<>());
        newCart.getCartElementList().add(cartElement);
        newCart.setTotalPrice(product.get().getPrice());
        LOGGER.info("Saving the cart to the database");
        return cartRepository.save(newCart);
    }

    @Override
    public Cart exploreCart(UserDetailsImp currentUser) {

        User user = userService.getUserById(currentUser.getId());

        return user.getCart();
    }

    @Override
    public void clearCart(UserDetailsImp currentUser) {
        User user = userService.getUserById(currentUser.getId());
        Cart cart = user.getCart();
        if(cart == null){
            LOGGER.error("Cart for Logged in user "+ currentUser + " is empty");
            throw  new InvalidArgumentException("Cart is already empty");
        }

        user.setCart(null);
        LOGGER.info("Updating logged in cart");
        userService.updateUser(user);
    }

    @Override
    public Cart increaseCartElement(Long elementId, Integer amount, UserDetailsImp currentUser){

        User user = userService.getUserById(currentUser.getId());

        Cart cart = user.getCart();

        if(cart == null){
            throw new NotFoundException("cart is empty");
        }

        CartElement selectedCartElement = getElementFromCart(cart, elementId);

        if(selectedCartElement == null){
            LOGGER.error("There is no item with selected id: " + elementId+ " in the cart");
            throw  new NotFoundException("There is no item with selected id: " + elementId+ " in the cart");
        }

        if(selectedCartElement.getQuantity() < amount) {
            LOGGER.error("you can not have quantity less than 0");
            throw new InvalidArgumentException("you can not have quantity less than 0");
        }
        selectedCartElement.setQuantity(selectedCartElement.getQuantity() + amount);
        cart.setTotalPrice(cart.getTotalPrice() + (selectedCartElement.getProduct().getPrice() *amount));
        LOGGER.info("Updating cart");
        return cartRepository.save(cart);
    }

    @Override
    public Cart decreaseCartElement(Long elementId, Integer amount, UserDetailsImp currentUser) {

        User user = userService.getUserById(currentUser.getId());

        Cart cart = user.getCart();

        if (cart == null) {
            throw new NotFoundException("Cart is empty");
        }

        CartElement selectedCartElement = getElementFromCart(cart, elementId);
        if(selectedCartElement == null){
            throw  new NotFoundException("There is no item with selected id in the cart");
        }

        if(selectedCartElement.getQuantity() < amount) {
            throw new InvalidArgumentException("you can not have quantity less than 0");
        }

        selectedCartElement.setQuantity(selectedCartElement.getQuantity() - amount);
        cart.setTotalPrice(cart.getTotalPrice() - (selectedCartElement.getProduct().getPrice() * amount));


//        List<CartElement> cartElementList = cart.getCartElementList();
//
//        for (CartElement cartElement : cartElementList) {
//            if (cartElement.getId().equals(elementId)){
//                cartElement.setQuantity(cartElement.getQuantity() - amount);
//                cart.setTotalPrice(cart.getTotalPrice() + (cartElement.getProduct().getPrice()* amount));
//            }
//        }
        return cartRepository.save(cart);
    }


    public CartElement getElementFromCart(Cart cart, Long elementId){
        List<CartElement> cartElementList = cart.getCartElementList();

        for(CartElement cartElement: cartElementList){
            if(cartElement.getId().equals(elementId)){
                return cartElement;
            }
        }

        return null;
    }


}
