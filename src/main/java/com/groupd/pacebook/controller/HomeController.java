package com.groupd.pacebook.controller;

import com.groupd.pacebook.dto.PostDto;
import com.groupd.pacebook.model.Post;
import com.groupd.pacebook.model.User;
import com.groupd.pacebook.service.PostService;
import com.groupd.pacebook.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public HomeController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public String home(Model model, Principal principal) {
        // Get current user email from Spring Security
        String currentUserEmail = principal.getName();

        // Load user data
        User currentUser = userService.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<PostDto> posts = postService.getFeedDtos(currentUser.getEmail());
        Set<User> friends = userService.getFriends(currentUser.getEmail());

        model.addAttribute("posts", posts);
        model.addAttribute("postDto", new PostDto());
        model.addAttribute("friends", friends);
        model.addAttribute("currentUser", currentUser);

        return "home";
    }


    @PostMapping("/friends/remove/{id}")
    public String removeFriend(@PathVariable("id") Long id, Principal principal) {
        userService.removeFriend(principal.getName(), id);
        return "redirect:/home";
    }

}
