package com.groupd.pacebook.controller;

import com.groupd.pacebook.model.User;
import com.groupd.pacebook.service.LikeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/toggle/{id}")
    @ResponseBody
    public Map<String, Object> toggleLike(@PathVariable("id") Long id, Principal principal) {
        int likeCount = likeService.toggleLikeAndReturnCount(id, principal.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("likeCount", likeCount);
        response.put("liked", likeService.isPostLikedByUser(id, principal.getName()));
        return response;
    }

}
