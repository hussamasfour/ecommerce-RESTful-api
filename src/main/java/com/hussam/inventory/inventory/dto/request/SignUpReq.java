package com.hussam.inventory.inventory.dto.request;

import javax.validation.constraints.*;
import java.util.Set;

public class SignUpReq {

    @NotEmpty(message = "firstname must be not empty")
    @Size(min = 5,max = 30)
    private String firstName;

    @NotEmpty(message = "lastName must be not empty")
    @Size(min = 5,max = 30)
    private String lastName;

    @Size(min = 10, max = 10)
    @Pattern(regexp = "(^$|[0-9]{10})")
    private String mobile;

    @NotEmpty(message = "User name must be not empty!")
    @Size(min = 6, max = 30)
    private String username;

    @NotEmpty(message = "Email must not be empty!")
    @Size(max = 40)
    @Email(message = "Please enter a valid email!")
    private String email;

    private Set<String> role;

    @NotEmpty(message = "Password must not be empty!")
    @Size(min = 8, max = 30)
    private String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRole() {
      return this.role;
    }

    public void setRole(Set<String> user) {
      this.role = user;
    }
}