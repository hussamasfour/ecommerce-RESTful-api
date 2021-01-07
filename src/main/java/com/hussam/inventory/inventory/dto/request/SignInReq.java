package com.hussam.inventory.inventory.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignInReq {
    @NotBlank
    @Size(min=6, max = 30)
    private String username;

    @NotBlank
    @Size(min = 8, max = 30)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}