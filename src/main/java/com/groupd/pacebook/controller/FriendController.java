package com.groupd.pacebook.controller;

import com.groupd.pacebook.model.FriendRequest;
import com.groupd.pacebook.model.User;
import com.groupd.pacebook.service.FriendService;
import com.groupd.pacebook.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;

    public FriendController(FriendService friendService, UserService userService) {
        this.friendService = friendService;
        this.userService = userService;
    }

    // ✅ Send friend request
    @PostMapping("/send/{receiverId}")
    public String sendFriendRequest(@PathVariable Long receiverId,
                                    @AuthenticationPrincipal UserDetails userDetails,
                                    Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User sender = userService.findByEmail(userDetails.getUsername());
        if (sender == null) {
            return "redirect:/login";
        }

        boolean success = friendService.sendFriendRequest(sender.getId(), receiverId);

        if (!success) {
            model.addAttribute("error", "Cannot send friend request.");
        }

        return "redirect:/users";
    }

    // ✅ Show incoming & outgoing friend requests
    @GetMapping("/requests")
    public String showFriendRequests(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User currentUser = userService.findByEmail(userDetails.getUsername());
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<FriendRequest> incomingRequests = friendService.getPendingRequestsForUser(currentUser.getId());
        List<FriendRequest> outgoingRequests = friendService.getPendingRequestsSentByUser(currentUser.getId());

        model.addAttribute("incomingRequests", incomingRequests);
        model.addAttribute("outgoingRequests", outgoingRequests);
        return "friend-requests";
    }

    // ✅ Accept friend request
    @PostMapping("/accept/{requestId}")
    public String acceptFriendRequest(@PathVariable Long requestId) {
        friendService.acceptFriendRequest(requestId);
        return "redirect:/friends/requests";
    }

    // ✅ Decline friend request
    @PostMapping("/decline/{requestId}")
    public String declineFriendRequest(@PathVariable Long requestId) {
        friendService.declineFriendRequest(requestId);
        return "redirect:/friends/requests";
    }
}
