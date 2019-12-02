package com.czaplon.trainer.controller;

import com.czaplon.trainer.dto.PasswordChangeForm;
import com.czaplon.trainer.dto.UserAdminForm;
import com.czaplon.trainer.model.User;
import com.czaplon.trainer.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserAdminController {

    private Logger logger = LoggerFactory.getLogger(UserAdminController.class);

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserAdminController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @ModelAttribute("username")
    public String getUserData(@AuthenticationPrincipal User user) {
        return user.getUsername();
    }

    @GetMapping("/")
    public String showForm(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", new UserAdminForm(user));
        return "useradmin";
    }

    @GetMapping("/password/")
    public String showPasswordForm(Model model) {
        model.addAttribute("passwordForm", new PasswordChangeForm());
        return "passadmin";
    }

    @PostMapping("/")
    public String saveUserChanges(@Valid @ModelAttribute("user") UserAdminForm userAdminForm, BindingResult result, @AuthenticationPrincipal User user) {
        if (result.hasErrors()) {
            logger.warn("Errors during user update");
            return "useradmin";
        }
        user.setName(userAdminForm.getName());
        user.setEmail(userAdminForm.getEmail());
        user.setHeight(userAdminForm.getHeight());
        userRepository.save(user);
        return "redirect:/";
    }

    @PostMapping("/password/")
    public String updatePassword(@Valid @ModelAttribute("passwordForm") PasswordChangeForm passwordChangeForm, BindingResult result, @AuthenticationPrincipal User user) {
        logger.info(passwordChangeForm.toString());
        if (!passwordChangeForm.isPasswordEquals()) {
            result.rejectValue("confirmPassword","error.confirmPassword","Passwords has to match");
        }
        if (!passwordEncoder.matches(passwordChangeForm.getOldPassword(), user.getPassword())) {
            result.rejectValue("oldPassword","error.oldPassword","Old password invalid");
        }

        if (result.hasErrors()) {
            return "passadmin";
        } else {
            user.setPassword(passwordEncoder.encode(passwordChangeForm.getNewPassword()));
            userRepository.save(user);
            return "redirect:/user/";
        }
    }

}
