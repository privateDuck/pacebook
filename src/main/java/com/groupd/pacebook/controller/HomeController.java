package com.groupd.pacebook.controller;

import com.groupd.pacebook.dto.PostDto;
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
    public String home(Model model) {
        String email = "john@example.com";
        List<PostDto> posts = postService.getFeedDtos(email);
        model.addAttribute("posts", posts);
        model.addAttribute("postDto", new PostDto());
        return "home";
    }
}
