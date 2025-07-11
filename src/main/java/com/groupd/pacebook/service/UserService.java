package com.groupd.pacebook.service;

import com.groupd.pacebook.model.User;
import com.groupd.pacebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepo;

    @Autowired
    public UserService(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    public User getUserById(Long id){
        return userRepo.findById(id).orElse(new User());
    }

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public User addUser(User user){
        return userRepo.save(user);
    }

    public void sendRequestFromUser1toUser2(Long id1, Long id2){
        User user1 = userRepo.findById(id1).orElse(new User());
        User user2 = userRepo.findById(id2).orElse(new User());

        user1.sendRequestTo(user2);
        userRepo.save(user1);
        userRepo.save(user2);
    }

    public void deleteUserById(Long id){
        userRepo.deleteById(id);
    }

    public void updateUser(User user){
        userRepo.save(user);
    }

    public Set<User> getFriends(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        return user.getFriends();
    }

    public void removeFriend(String email, Long id) {
        User user = userRepo.findByEmail(email).orElseThrow();
        User friend = userRepo.findById(id).orElseThrow();

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);

        userRepo.save(user);
        userRepo.save(friend);
    }

}
