package com.groupd.pacebook.controller;

import com.groupd.pacebook.dto.PostDto;
import com.groupd.pacebook.model.User;
import com.groupd.pacebook.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, BindingResult result, Model model) {
        // No @Valid so manual validation if needed

        // Example simple validation (optional)
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            result.rejectValue("email", null, "Email is required");
            return "register";
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            result.rejectValue("password", null, "Password is required");
            return "register";
        }

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            result.rejectValue("email", null, "Email is already registered");
            return "register";
        }

        userService.registerUser(user);
        return "redirect:/login";
    }


    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }
}
