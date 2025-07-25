package com.groupd.pacebook.service;

import com.groupd.pacebook.model.Post;
import com.groupd.pacebook.model.User;
import com.groupd.pacebook.repository.PostRepository;
import com.groupd.pacebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    public final PostRepository postRepository;
    public final UserRepository userRepository;


    @Autowired
    public LikeService(PostRepository postRepository,UserRepository userRepository){
        this.postRepository= postRepository;
        this.userRepository = userRepository;
    }


    public int toggleLikeAndReturnCount(Long postId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        if (post.getLikedBy().remove(user)) {
            user.getLikedPosts().remove(post);
        } else {
            post.getLikedBy().add(user);
            user.getLikedPosts().add(post);
        }
        postRepository.save(post);
        return post.getLikedBy().size();
    }

    public boolean isPostLikedByUser(Long postId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();
        return post.getLikedBy().contains(user);
    }

}
