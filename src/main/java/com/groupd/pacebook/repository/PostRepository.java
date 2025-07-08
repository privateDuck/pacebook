package com.groupd.pacebook.repository;

import com.groupd.pacebook.model.Post;
import com.groupd.pacebook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.author IN :users ORDER BY p.timestamp DESC")
    List<Post> findPostsForFeed(@Param("users") List<User> users);
}
