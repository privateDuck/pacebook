package com.groupd.pacebook.service;

import com.groupd.pacebook.model.Post;
import com.groupd.pacebook.model.User;
import com.groupd.pacebook.repository.PostRepository;
import com.groupd.pacebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<Post> getFeedForUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();

        // Get user's friends + self
        Set<User> feedAuthors = new HashSet<>(user.getFriends());
        feedAuthors.add(user);

        return postRepository.findPostsForFeed(new ArrayList<>(feedAuthors));
    }

    public void savePost(String content, String email) {
        User author = userRepository.findByEmail(email).orElseThrow();
        Post post = new Post();
        post.setAuthor(author);
        post.setContent(content);
        post.setTimestamp(LocalDateTime.now());
        postRepository.save(post);
    }
}
