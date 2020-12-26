package com.hussam.inventory.inventory.dto.request;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PasswordResetRequest {

    @NotBlank
    @Size(min = 8, max = 30, message = "password must be between 8-30 length")
    private String oldPassword;

    @NotBlank
    @Size(min = 8,max = 30, message = "password must be between 8-30 length")
    private String newPassword;

    @NotBlank
    @Size(min = 8,max = 30, message = "password must be between 8-30 length")
    private String newPasswordConfirm;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }
}
