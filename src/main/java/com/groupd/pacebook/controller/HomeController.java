package com.groupd.pacebook.controller;

import com.groupd.pacebook.model.Post;
import com.groupd.pacebook.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final PostService postService;

    public HomeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String home(Model model, Principal principal) {
        List<Post> posts = postService.getFeedForUser(principal.getName());
        model.addAttribute("posts", posts);
        model.addAttribute("postDto", new Post());
        return "home";
    }
}
