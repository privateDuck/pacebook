package com.groupd.pacebook.repository;

import com.groupd.pacebook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /*Optional<User> findByUsername(String username);
    Optional<List<User>> findAllByOrderByUsernameAsc();
    Optional<List<User>> findAllFriendsByUsername(String username);*/

    Optional <User> findByEmail(String email);
}
