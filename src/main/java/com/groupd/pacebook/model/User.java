package com.groupd.pacebook.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Entity
@Component
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    // One-to-Many with Post
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    // Likes (Many-to-Many)
    @ManyToMany
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> likedPosts = new HashSet<>();

    // Friends (bidirectional Many-to-Many)
    @ManyToMany
    @JoinTable(
            name = "friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<User> friends = new HashSet<>();

    // Friend Requests
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FriendRequest> sentRequests = new HashSet<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FriendRequest> receivedRequests = new HashSet<>();

    public User() {
        this.username = "none";
        this.email = "";
        this.password = "";
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public Set<Post> getLikedPosts() {
        return likedPosts;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void addFriend(User user) {
        this.friends.add(user);
        user.getFriends().add(this); // Ensure bidirectional friendship
    }

    public void removeFriend(User user) {
        this.friends.remove(user);
        user.getFriends().remove(this);
    }

    public Set<FriendRequest> getSentRequests() {
        return sentRequests;
    }

    public Set<FriendRequest> getReceivedRequests() {
        return receivedRequests;
    }
}
