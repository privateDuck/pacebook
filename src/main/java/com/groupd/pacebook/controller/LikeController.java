package com.groupd.pacebook.controller;

import com.groupd.pacebook.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/toggle/{id}")
    public String addlikes(@PathVariable("id") Long id, String email) {
        email = "jane@example.com";
        likeService.likePost(id, email);
        return "redirect:/home";
    }
}
