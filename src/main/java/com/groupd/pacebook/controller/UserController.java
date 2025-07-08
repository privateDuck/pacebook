package com.groupd.pacebook.controller;

import com.groupd.pacebook.model.User;
import com.groupd.pacebook.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String homePage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody User user){
        User added = userService.addUser(user);
        return ResponseEntity.ok(added);
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }
}
