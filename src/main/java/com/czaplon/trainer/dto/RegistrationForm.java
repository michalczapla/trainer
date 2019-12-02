package com.czaplon.trainer.dto;



import com.czaplon.trainer.model.User;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegistrationForm {

    @NotNull(message = "Username cannot be empty")
    @Size(min = 3, message = "Username cannot be shorten than 3 characters")
    private String username;

    @NotNull(message = "Name cannot be empty")
    @Size(min = 1, message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Password cannot be empty")
    @Size(min = 1, message = "Password cannot be empty")
    private String password;

    @NotNull(message = "Password cannot be empty")
    @Size(min = 1, message = "Password cannot be empty")
    private String confirmPassword;
    private String email;

    @NotNull(message = "Height cannot be empty")
    @Min(value = 0, message = "Height cannot be negative")
    private Float height;

    public RegistrationForm() {
    }

    public RegistrationForm(String username, String name, String password, String confirmPassword, String email, Float height) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
        this.height = height;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public User toUser(){
        return new User(this.username,this.name,this.password,this.email,this.height);
    }

    public boolean isPasswordEquals() {
        return password.equals(confirmPassword);
    }

    @Override
    public String toString() {
        return "RegistrationForm{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", email='" + email + '\'' +
                ", height=" + height +
                '}';
    }
}
