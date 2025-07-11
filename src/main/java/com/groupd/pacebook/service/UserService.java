package com.groupd.pacebook.service;

import com.groupd.pacebook.model.User;
import com.groupd.pacebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;
    @Autowired
    private User user;

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

    public User findByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }
}
