package com.groupd.pacebook.controller;

import com.groupd.pacebook.dto.PostDto;
import com.groupd.pacebook.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public String createPost(@ModelAttribute PostDto postDto) {
        postService.savePost(postDto.getContent(), postDto.getTitle(),"john@example.com");
        return "redirect:/home";
    }



}
