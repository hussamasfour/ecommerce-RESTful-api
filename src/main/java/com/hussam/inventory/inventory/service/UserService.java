package com.hussam.inventory.inventory.service;

import com.hussam.inventory.inventory.dto.request.PasswordResetRequest;
import com.hussam.inventory.inventory.dto.request.SignInReq;
import com.hussam.inventory.inventory.dto.request.SignUpReq;
import com.hussam.inventory.inventory.dto.response.JwtResponse;
import com.hussam.inventory.inventory.entities.User;
import com.hussam.inventory.inventory.security.services.UserDetailsImp;


import java.util.List;
import java.util.Optional;

public interface UserService {

    public void addUser(SignUpReq signUpForm);
    public User getUserById(Long id);

    public List<User> getUsers();

    public User update(User user);

    public void delete(Long id);

    JwtResponse authUser(SignInReq signInReq);
    boolean existByUsername(String username);

    boolean existByEmail(String email);

    Optional<User> getUserByUsername(String username);

    User updateUser(User user);

    void resetPassword(PasswordResetRequest passwordResetRequest, UserDetailsImp currentUser);
}
