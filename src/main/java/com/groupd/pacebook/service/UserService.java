package com.groupd.pacebook.service;

import com.groupd.pacebook.model.User;
import com.groupd.pacebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
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

    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("USER"))
        );
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
