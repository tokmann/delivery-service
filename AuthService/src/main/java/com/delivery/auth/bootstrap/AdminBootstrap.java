package com.delivery.auth.bootstrap;

import com.delivery.auth.model.User;
import com.delivery.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminBootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminBootstrap(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        String adminLogin = "admin123";
        String adminEmail = "admin@example.com";
        String adminPassword = "admin123";

        boolean adminExists = userRepository.findByLogin(adminLogin).isPresent();
        if (!adminExists) {
            User admin = new User();
            admin.setLogin(adminLogin);
            admin.setEmail(adminEmail);
            admin.setPasswordHash(passwordEncoder.encode(adminPassword));
            admin.setRole("ADMIN");
            userRepository.save(admin);
            System.out.println("Admin user created: " + adminLogin);
        } else {
            System.out.println("Admin user already exists: " + adminLogin);
        }
    }
}
