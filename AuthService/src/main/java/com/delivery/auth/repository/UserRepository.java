package com.delivery.auth.repository;

import com.delivery.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByLogin(String login);
    boolean existsByLogin(String login);
}
