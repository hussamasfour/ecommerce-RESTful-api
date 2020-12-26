package com.hussam.inventory.inventory.controllers;

import com.hussam.inventory.inventory.dto.request.SignInReq;
import com.hussam.inventory.inventory.dto.request.SignUpReq;
import com.hussam.inventory.inventory.dto.response.JwtResponse;
import com.hussam.inventory.inventory.dto.response.ResponseMessage;
import com.hussam.inventory.inventory.service.UserService;
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

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpReq signUpRequest) {
        userService.addUser(signUpRequest);
        return new ResponseEntity<>(new ResponseMessage("User Registered successfully"), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInReq signInRequest) {
        JwtResponse jwtResponse = userService.authUser(signInRequest);

        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }
}
