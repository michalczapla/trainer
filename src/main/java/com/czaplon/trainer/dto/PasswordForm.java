package com.czaplon.trainer.dto;

import com.czaplon.trainer.model.User;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public abstract class PasswordForm {
    @NotNull(message = "Password cannot be empty")
    @Size(min = 1, message = "Password cannot be empty")
    @Length(min = 5,message = "Password needs to be at least 5 character long")
    private String password;

    @NotNull(message = "Password cannot be empty")
    @Size(min = 1, message = "Password cannot be empty")
    @Length(min = 5,message = "Password needs to be at least 5 character long")
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
