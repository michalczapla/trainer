package com.czaplon.trainer.dto;

import com.czaplon.trainer.model.User;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserAdminForm {
    private String username;

    @NotNull(message = "Name cannot be empty")
    @Size(min = 1, message = "Name cannot be empty")
    private String name;
    private String email;

    @NotNull(message = "Height cannot be empty")
    @Min(value = 0, message = "Height cannot be negative")
    private Float height;

    public UserAdminForm() {
    }

    public UserAdminForm(String username, String name, String email, Float height) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.height = height;
    }

    public UserAdminForm(User user) {
        this.username=user.getUsername();
        this.name=user.getName();
        this.email=user.getEmail();
        this.height=user.getHeight();
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
}
