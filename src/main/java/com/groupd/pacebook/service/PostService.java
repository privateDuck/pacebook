package com.groupd.pacebook.service;

import com.groupd.pacebook.dto.PostDto;
import com.groupd.pacebook.model.Post;
import com.groupd.pacebook.model.User;
import com.groupd.pacebook.repository.PostRepository;
import com.groupd.pacebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public PostDto convertToDto(Post post, User currentUser) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setAuthorName(post.getAuthor().getUsername());
        dto.setTimestamp(post.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        dto.setLikeCount(post.getLikedBy().size());
        dto.setLikedByCurrentUser(post.getLikedBy().contains(currentUser));
        return dto;
    }

    public List<PostDto> getFeedDtos(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Set<User> authors = new HashSet<>(user.getFriends());
        authors.add(user);
        List<Post> posts = postRepository.findPostsForFeed(new ArrayList<>(authors));
        return posts.stream().map(post -> convertToDto(post, user)).toList();
    }

    public void savePost(String content, String title, String email) {
        User author = userRepository.findByEmail(email).orElseThrow();
        Post post = new Post();
        post.setAuthor(author);
        post.setTitle(title);
        post.setContent(content);
        post.setTimestamp(LocalDateTime.now());
        postRepository.save(post);
    }


}
