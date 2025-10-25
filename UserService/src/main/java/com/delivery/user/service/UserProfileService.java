package com.delivery.user.service;

import com.delivery.user.model.UserProfile;
import com.delivery.user.repository.UserProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public Optional<UserProfile> getUserProfile(String userId) {
        return userProfileRepository.findById(userId);
    }

    public UserProfile createOrUpdateProfile(String userId, String email) {
        return userProfileRepository.findById(userId)
                .orElseGet(() -> {
                    System.out.println("Creating new profile for user: " + userId);
                    UserProfile newProfile = new UserProfile(userId, email);
                    UserProfile saved = userProfileRepository.save(newProfile);
                    System.out.println("Profile created: " + saved.getUserId());
                    return saved;
                });
    }

    public UserProfile updateProfile(String userId, String firstName, String lastName, String phoneNumber) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setPhoneNumber(phoneNumber);
        profile.setUpdatedAt(java.time.LocalDateTime.now());

        return userProfileRepository.save(profile);
    }

    public UserProfile addAddress(String userId, String address) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        profile.getAddresses().add(address);
        profile.setUpdatedAt(java.time.LocalDateTime.now());

        return userProfileRepository.save(profile);
    }

}
