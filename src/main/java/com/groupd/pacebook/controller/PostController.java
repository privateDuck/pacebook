package com.groupd.pacebook.controller;

import com.groupd.pacebook.model.Post;
import com.groupd.pacebook.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public String createPost(@ModelAttribute Post postDto, Principal principal) {
        postService.savePost(postDto.getContent(), principal.getName());
        return "redirect:/home";
    }
}
