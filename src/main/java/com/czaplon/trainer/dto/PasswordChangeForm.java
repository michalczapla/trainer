package com.czaplon.trainer.dto;

import com.czaplon.trainer.model.User;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PasswordChangeForm {

    @NotNull(message = "Old password cannot be empty")
    @Size(min = 1, message = "Old password cannot be empty")
    private String oldPassword;

    @NotNull(message = "New password cannot be empty")
    @Size(min = 1, message = "New password cannot be empty")
    @Length(min = 5,message = "Password needs to be at least 5 character long")
    private String newPassword;

    @NotNull(message = "New password cannot be empty")
    @Size(min = 1, message = "New password cannot be empty")
    @Length(min = 5,message = "Password needs to be at least 5 character long")
    private String confirmPassword;

    public PasswordChangeForm() {
    }

    public PasswordChangeForm(String oldPassword, String newPassword, String confirmPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public boolean isPasswordEquals() {
        return newPassword.equals(confirmPassword);
    }

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "PasswordChangeForm{" +
                "oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
