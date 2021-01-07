package com.hussam.inventory.inventory.service.impl;

import com.hussam.inventory.inventory.controllers.UserController;
import com.hussam.inventory.inventory.dto.request.PasswordResetRequest;
import com.hussam.inventory.inventory.dto.request.SignInReq;
import com.hussam.inventory.inventory.dto.request.SignUpReq;
import com.hussam.inventory.inventory.dto.response.JwtResponse;
import com.hussam.inventory.inventory.entities.*;
//import com.hussam.inventory.inventory.repositories.ProductRepository;
import com.hussam.inventory.inventory.exception.InvalidArgumentException;
import com.hussam.inventory.inventory.exception.NotFoundException;
import com.hussam.inventory.inventory.repositories.RoleRepository;
import com.hussam.inventory.inventory.repositories.UserRepository;
import com.hussam.inventory.inventory.security.jwt.JwtUtils;
//import com.hussam.inventory.inventory.service.SaleService;
import com.hussam.inventory.inventory.security.services.UserDetailsImp;
import com.hussam.inventory.inventory.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImp.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    JwtUtils jwtProvider;

    // Create new User
    @Override
    public void addUser(SignUpReq signUpRequest) {
        // Check if username is already existed!!
        if (userRepository.existsByUsername(signUpRequest.getUsername()))
            throw new DataIntegrityViolationException("username is already exist");

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new DataIntegrityViolationException("Email is already exist");
        }

        Set<Role> roles = new HashSet<>();
        Set<String> strRoles = signUpRequest.getRole();

        //Create the user object
        User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()),signUpRequest.getEmail(), new Date(), signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getMobile()) ;

        //Check the user role
        if( strRoles == null ) {
            LOGGER.info("Fetching role for roleType");
            Role userRole = roleRepository.findByRoleType(RoleType.ROLE_USER)
                    .orElseThrow(() -> new DataIntegrityViolationException("User Role not found."));
            roles.add(userRole);

        } else  {
            strRoles.forEach(role -> {
                if ("admin".equals(role)) {
                    LOGGER.info("Fetching role for roleType");
                    Role adminRole = roleRepository.findByRoleType(RoleType.ROLE_ADMIN)
                            .orElseThrow(() -> new DataIntegrityViolationException("Role is not found."));
                    roles.add(adminRole);
                } else {
                    LOGGER.info("Fetching role for roleType");
                    Role userRole = roleRepository.findByRoleType(RoleType.ROLE_USER)
                            .orElseThrow(() -> new DataIntegrityViolationException("Role is not found!."));
                    roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);

        LOGGER.info("Saving the User:" + user);
        userRepository.save(user);
    }

    @Override
    public User getUserById(Long id){
       LOGGER.info("Fetching user for user id:" + id);
       Optional<User> user = userRepository.findById(id);
       if(!user.isPresent()){
           LOGGER.error("User With id:"+ id + " is not found" );
           throw new NotFoundException("User not found");
       }
       return user.get();
    }

    @Override
    public List<User> getUsers(){
        LOGGER.info("Fetching all Users");
        return userRepository.findAll();
    }

    @Override
    public User update(User user) {
        LOGGER.info("Update User Info and Save it");
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        LOGGER.info("Deleting user with id:" + id);
        userRepository.deleteById(id);
    }

    @Override
    public JwtResponse authUser(SignInReq signInReq) {

        // authenticate the login request username and password

        LOGGER.info("Authenticating username and password");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInReq.getUsername(), signInReq.getPassword()));

        LOGGER.info("Getting logged in user details");
        UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        LOGGER.info("Generating the JWT token");
        String jwt = jwtProvider.generateJwtToken(userDetails);

        LOGGER.info("Getting the user role types ");
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        LOGGER.info("Successfully logged in and returning JWT response");
       return new JwtResponse(jwt, userDetails.getId(),userDetails.getUsername(),userDetails.getEmail(), roles);
    }

    @Override
    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void resetPassword(PasswordResetRequest passwordResetRequest, UserDetailsImp currentUser) {
        User user = getUserById(currentUser.getId());

        if(!encoder.matches(passwordResetRequest.getOldPassword(), user.getPassword())){
            throw new InvalidArgumentException("Entered password does not match your password");
        }

        user.setPassword(encoder.encode(passwordResetRequest.getNewPassword()));
        userRepository.save(user);
    }

}
