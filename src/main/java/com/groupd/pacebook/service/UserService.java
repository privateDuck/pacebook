package com.groupd.pacebook.service;

import com.groupd.pacebook.model.User;
import com.groupd.pacebook.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Find user by email (used for authentication and friend requests)
    public User findByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.orElse(null);  // return null if not found, or throw exception if you prefer
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElse(new User());
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user){
        return userRepository.save(user);
    }

    // Your existing send request method (you may replace with FriendService logic later)
    public void sendRequestFromUser1toUser2(Long id1, Long id2){
        User user1 = userRepository.findById(id1).orElse(new User());
        User user2 = userRepository.findById(id2).orElse(new User());

//
        userRepository.save(user1);
        userRepository.save(user2);
    }

    public void deleteUserById(Long id){
        userRepository.deleteById(id);
    }

    public void updateUser(User user){
        userRepository.save(user);
    }
}
