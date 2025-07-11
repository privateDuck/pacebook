package com.groupd.pacebook.controller;

import com.groupd.pacebook.model.User;
import com.groupd.pacebook.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
public class UserController {

    private final UserService userService;
    private final FriendService friendService;

    public UserController(UserService userService, FriendService friendService) {
        this.userService = userService;
        this.friendService = friendService;
    }

    // ✅ Show all users except the current user (User Directory)
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

    // ✅ Send friend request (linked to "Send Request" button in directory)
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

    // Optional: You can add more methods here if needed
}