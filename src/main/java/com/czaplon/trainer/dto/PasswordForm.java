package com.czaplon.trainer.dto;

import com.czaplon.trainer.model.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PasswordForm {
    @NotNull(message = "Password cannot be empty")
    @Size(min = 1, message = "Password cannot be empty")
    private String password;

    @NotNull(message = "Password cannot be empty")
    @Size(min = 1, message = "Password cannot be empty")
    private String confirmPassword;

    public PasswordForm() {
    }

    public PasswordForm(String password, String confirmPassword) {
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public boolean isPasswordEquals() {
        return password.equals(confirmPassword);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
