package com.groupd.pacebook.controller;

import com.groupd.pacebook.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/likes")
public class LikeController {

    private final PostService postService;

    @Autowired
    public LikeController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/toggle/{id}")
    public addlikes(@PathVariable Long id) {

    }
}
