package com.groupd.pacebook.controller;

import com.groupd.pacebook.model.User;
import com.groupd.pacebook.service.FriendService;
import com.groupd.pacebook.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

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

        User currentUser = userService.findByEmail(userDetails.getUsername()).orElse(null);

        if (currentUser == null) {
            return "redirect:/login";
        }

        List<User> users = userService.getUsers().stream()
                .filter(user -> !user.getId().equals(currentUser.getId()))
                .toList();

        List<User> requestingUsers = friendService.getRequestingUsers(currentUser.getId());
        List<User> requestedUsers = friendService.getRequestedUsers(currentUser.getId());

        model.addAttribute("users", users);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("requestingUsers", requestingUsers);
        model.addAttribute("requestedUsers", requestedUsers);
        return "user-directory";
    }

    @PostMapping("/users/send-request/{receiverId}")
    public String sendFriendRequest(@PathVariable Long receiverId,
                                    @AuthenticationPrincipal UserDetails userDetails,
                                    Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User sender = userService.findByEmail(userDetails.getUsername()).orElse(null);

        if (sender == null) {
            return "redirect:/login";
        }
        boolean success = friendService.sendFriendRequest(sender.getId(), receiverId);

        if (!success) {
            model.addAttribute("error", "Friend request failed or already sent.");
        }

        return "redirect:/users"; // Redirect back to user directory
    }

//    @GetMapping("/requests")
//    public String showFriendRequests(@AuthenticationPrincipal UserDetails userDetails, Model model) {
//        if (userDetails == null) {
//            return "redirect:/login";
//        }
//
//        User currentUser = userService.findByEmail(userDetails.getUsername()).orElse(null);
//        if (currentUser == null) {
//            return "redirect:/login";
//        }
//
//        List<User> requestingUsers = friendService.getRequestingUsers(currentUser.getId());
//        List<User> requestedUsers = friendService.getRequestedUsers(currentUser.getId());
//
//        model.addAttribute("requestingUsers", requestingUsers);
//        model.addAttribute("requestedUsers", requestedUsers);
//        return "user-directory";
//
//    }


    @PostMapping("/accept/{user_id}")
    public String acceptFriendRequest(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long user_id) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User currentUser = userService.findByEmail(userDetails.getUsername()).orElse(null);
        if (currentUser == null) {
            return "redirect:/login";
        }

        friendService.acceptFriendRequest(currentUser.getId(), user_id);
        return "redirect:/users";
    }

    @PostMapping("/decline/{user_id}")
    public String declineFriendRequest(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long user_id) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User currentUser = userService.findByEmail(userDetails.getUsername()).orElse(null);
        if (currentUser == null) {
            return "redirect:/login";
        }

        friendService.declineFriendRequest(currentUser.getId(), user_id);
        return "redirect:/users";
    }

    @GetMapping("/error")
    public String errorPage(Model model) {
        return "error";
    }
}
