package com.groupd.pacebook.model;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;

    // One-to-Many with Post
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    // Many-to-Many Likes
    @ManyToMany
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> likedPosts = new HashSet<>();

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

    // ==== Constructors ====
    public User() {
        // No-args constructor required by JPA
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // ==== Getters and Setters ====

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Set<Post> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(Set<Post> likedPosts) {
        this.likedPosts = likedPosts;
    }

    public Set<User> getSentRequests() {
        return sentRequests;
    }

    public void setSentRequests(Set<User> sentRequests) {
        this.sentRequests = sentRequests;
    }

    public Set<User> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(Set<User> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    // ==== Convenience Methods ====

    public void addFriend(User user) {
        this.friends.add(user);
        user.getFriends().add(this);
    }

    public void sendRequestTo(User user) {
        this.sentRequests.add(user);
        user.getReceivedRequests().add(this);
    }

    public void likePost(Post post) {
        this.likedPosts.add(post);
        post.getLikedBy().add(this);
    }

    public void unlikePost(Post post) {
        this.likedPosts.remove(post);
        post.getLikedBy().remove(this);
    }
}
