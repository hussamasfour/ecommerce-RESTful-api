package com.hussam.inventory.inventory.controllers;

import com.hussam.inventory.inventory.dto.request.PasswordResetRequest;
import com.hussam.inventory.inventory.entities.User;
import com.hussam.inventory.inventory.exception.NotFoundException;
import com.hussam.inventory.inventory.dto.response.ResponseMessage;
import com.hussam.inventory.inventory.security.services.CurrentUser;
import com.hussam.inventory.inventory.security.services.UserDetailsImp;
import com.hussam.inventory.inventory.service.OrderService;
import com.hussam.inventory.inventory.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/users")
@CrossOrigin()
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getCurrentUser(@CurrentUser UserDetailsImp currentUser){
        LOGGER.info("Inside: getCurrentUser()");
        User user = userService.getUserById(currentUser.getId());

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers(){
        LOGGER.info("Inside: getALlUsers()");

        List<User> users = userService.getUsers();
        if(users.isEmpty()) {
            throw new NotFoundException("There is no user registered yet! ");
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping("/{uId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeUserById(@PathVariable("uId") Long id){
        LOGGER.info("Inside: removeUserById()"+ id);

        userService.delete(id);

        return new ResponseEntity<> (new ResponseMessage("User with id "+ id +" is successfully deleted"),HttpStatus.OK);
    }


    @PostMapping("/password/reset")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> passwordReset(@RequestBody @Valid PasswordResetRequest passwordResetRequest, @CurrentUser UserDetailsImp currentUser){
        LOGGER.info("Inside: passwordReset()" + passwordResetRequest);
        userService.resetPassword(passwordResetRequest, currentUser);
        return new ResponseEntity<>(new ResponseMessage("Password is successfully updated!"), HttpStatus.OK);
    }
}
