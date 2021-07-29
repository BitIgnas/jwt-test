package com.security.jwt.repository;

import com.security.jwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findUserByUsernameAndPassword(String username, String password);
    Optional<User> findUserByEmail(String email);
}
