package com.czaplon.trainer.dto;

import com.czaplon.trainer.model.User;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PasswordChangeForm extends PasswordForm{

    @NotNull(message = "Old password cannot be empty")
    @Size(min = 1, message = "Old password cannot be empty")
    private String oldPassword;

//    @NotNull(message = "New password cannot be empty")
//    @Size(min = 1, message = "New password cannot be empty")
//    @Length(min = 5,message = "Password needs to be at least 5 character long")
//    private String newPassword;
//
//    @NotNull(message = "New password cannot be empty")
//    @Size(min = 1, message = "New password cannot be empty")
//    @Length(min = 5,message = "Password needs to be at least 5 character long")
//    private String confirmPassword;

    public PasswordChangeForm() {
    }

    public PasswordChangeForm(String oldPassword, String newPassword, String confirmPassword) {
        super(newPassword, confirmPassword);
        this.oldPassword = oldPassword;
//        this.newPassword = newPassword;
//        this.confirmPassword = confirmPassword;
    }

//    public boolean isPasswordEquals() {
//        return newPassword.equals(confirmPassword);
//    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

//    public String getNewPassword() {
//        return getPassword();
//    }
//
//    public void setNewPassword(String newPassword) {
//        setPassword(newPassword);
//    }

//    public String getConfirmPassword() {
//        return getConfirmPassword();
//    }
//
//    public void setConfirmPassword(String confirmPassword) {
//        setConfirmPassword(confirmPassword);
//    }

    @Override
    public String toString() {
        return "PasswordChangeForm{" +
                "oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + getPassword() + '\'' +
                ", confirmPassword='" + getConfirmPassword() + '\'' +
                '}';
    }
}
