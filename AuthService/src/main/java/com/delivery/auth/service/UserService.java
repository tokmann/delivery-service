package com.delivery.auth.service;


import com.delivery.auth.model.User;
import com.delivery.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String login, String password, String email, String role) {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User(login, passwordEncoder.encode(password), email, role);
        return userRepository.save(user);
    }

    public User authenticate(String login, String password) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}
