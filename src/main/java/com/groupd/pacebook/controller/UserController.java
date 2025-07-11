package com.groupd.pacebook.controller;

import com.groupd.pacebook.model.User;
import com.groupd.pacebook.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> index(){
        return ResponseEntity.ok(userService.getUsers());
    }

//    @PostMapping("/signup")
//    public ResponseEntity<User> signUp(@RequestBody User user){
//        User added = userService.addUser(user);
//        return ResponseEntity.ok(added);
//    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User loginRequest, Model model) {
        User user = userService.findByEmail(loginRequest.getEmail());
        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            return "redirect:/home"; // Redirect to home page after successful login
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            userService.addUser(user);
            return "redirect:/login?success";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/home")
    public String showHomePage() {
        return "home";
    }
}
