package com.hussam.inventory.inventory.controllers;

import com.hussam.inventory.inventory.dto.request.SignInReq;
import com.hussam.inventory.inventory.dto.request.SignUpReq;
import com.hussam.inventory.inventory.dto.response.JwtResponse;
import com.hussam.inventory.inventory.dto.response.ResponseMessage;
import com.hussam.inventory.inventory.service.UserService;
import com.hussam.inventory.inventory.service.impl.CartServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@CrossOrigin
public class AccessController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpReq signUpRequest) {
        LOGGER.info("Inside RegisterUser() for " + signUpRequest);
        userService.addUser(signUpRequest);
        return new ResponseEntity<>(new ResponseMessage("User Registered successfully"), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInReq signInRequest) {
        LOGGER.info("Inside authenticateUser() for "+ signInRequest);

        JwtResponse jwtResponse = userService.authUser(signInRequest);

        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }
}
