package com.groupd.pacebook.controller;

import com.groupd.pacebook.model.User;
import com.groupd.pacebook.service.LikeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/toggle/{id}")
    public String addLikes(@PathVariable("id") Long id, Principal principal) {
        likeService.likePost(id, principal.getName());
        return "redirect:/home";
    }

}
