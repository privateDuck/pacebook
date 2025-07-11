package com.groupd.pacebook.controller;

import com.groupd.pacebook.dto.PostDto;
import com.groupd.pacebook.model.Post;
import com.groupd.pacebook.model.User;
import com.groupd.pacebook.service.PostService;
import com.groupd.pacebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final PostService postService;
    private final UserService userService;
    String email = "john@example.com";

    @Autowired
    public HomeController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public String home(Model model) {
        List<PostDto> posts = postService.getFeedDtos(email);
        Set<User> friends = userService.getFriends(email);
        model.addAttribute("posts", posts);
        model.addAttribute("postDto", new PostDto());
        model.addAttribute("friends", friends);
        return "home";
    }

    @PostMapping("/friends/remove/{id}")
    public String removeFriend(@PathVariable("id") Long id) {
        userService.removeFriend(email, id);
        return "redirect:/home";
    }

}
