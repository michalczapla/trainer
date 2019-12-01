package com.czaplon.trainer.controller;

import com.czaplon.trainer.dto.RegistrationForm;
import com.czaplon.trainer.model.User;
import com.czaplon.trainer.repository.UserRepository;
import com.czaplon.trainer.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;


@Controller
@RequestMapping("/register")
public class RegisterController {

    private Logger logger = LoggerFactory.getLogger(RegisterController.class);

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    @Autowired
    public RegisterController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getRegisterForm(RegistrationForm registrationForm) {
        return "register";
    }

    @PostMapping
    public String createUser(@Valid RegistrationForm registrationForm, BindingResult result) {
        logger.info(registrationForm.toString());
        if (result.hasErrors() || !registrationForm.isPasswordEquals()) {
            if (!registrationForm.isPasswordEquals())
                result.rejectValue("confirmPassword","error.confirmPassword","Passwords has to match");
            return "register";
        }
        User currentUser = userRepository.findByUsername(registrationForm.getUsername());
        if (currentUser!=null) {
            result.rejectValue("username","error.username","User already exists");
            return "register";
        }

        User newUser  = registrationForm.toUser();
        newUser.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
        userRepository.save(newUser);
        logger.info(newUser.toString());
    return "redirect:/login";
    }
}
