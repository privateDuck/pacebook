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
    private final FriendService friendService;

    public UserController(UserService userService, FriendService friendService) {
        this.userService = userService;
        this.friendService = friendService;
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

    // Kaushika's additions
    @GetMapping("/users")
    public String showAllUsers(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login"; // Redirect if user is not authenticated
        }

        User currentUser = userService.findByEmail(userDetails.getUsername());

        List<User> users = userService.getUsers().stream()
                .filter(user -> !user.getId().equals(currentUser.getId()))
                .toList();

        model.addAttribute("users", users);
        model.addAttribute("currentUser", currentUser);
        return "user-directory";
    }

    @PostMapping("/users/send-request/{receiverId}")
    public String sendFriendRequest(@PathVariable Long receiverId,
                                    @AuthenticationPrincipal UserDetails userDetails,
                                    Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User sender = userService.findByEmail(userDetails.getUsername());

        boolean success = friendService.sendFriendRequest(sender.getId(), receiverId);

        if (!success) {
            model.addAttribute("error", "Friend request failed or already sent.");
        }

        return "redirect:/users"; // Redirect back to user directory
    }
}
