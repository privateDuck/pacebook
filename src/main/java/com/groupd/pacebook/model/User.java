package com.groupd.pacebook.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private List<Post> posts = new ArrayList<Post>();

    // Many-to-Many Likes
    @ManyToMany
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> likedPosts = new HashSet<Post>();

    // Friend Requests
    @ManyToMany
    @JoinTable(
            name = "friend_requests",
            joinColumns = @JoinColumn(name = "sender_id"),
            inverseJoinColumns = @JoinColumn(name = "receiver_id")
    )
    private Set<User> sentRequests = new HashSet<>();

    @ManyToMany(mappedBy = "sentRequests")
    private Set<User> receivedRequests = new HashSet<>();

    // Friends
    @ManyToMany
    @JoinTable(
            name = "friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<User> friends = new HashSet<>();

    public User(){
        this.sentRequests = new HashSet<>();
        this.receivedRequests = new HashSet<>();
        this.friends = new HashSet<>();
        this.posts = new ArrayList<>();
        this.likedPosts = new HashSet<>();
        this.username = "none";
        this.email = "";
        this.password = "";
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public  Set<User> getFriends() {
        return friends;
    }

    public Set<User> getSentRequests() {
        return sentRequests;
    }

    public Set<User> getReceivedRequests() {
        return receivedRequests;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void addFriend(User user) {
        this.friends.add(user);
    }

    public void sendRequestTo(User user) {
        this.sentRequests.add(user);
    }
}