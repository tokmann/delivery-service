package com.delivery.user.controller;


import com.delivery.user.model.AddAddressRequest;
import com.delivery.user.model.CreateUserRequest;
import com.delivery.user.model.UpdateProfileRequest;
import com.delivery.user.model.UserProfile;
import com.delivery.user.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserProfileService userProfileService;

    public UserController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfile> getMyProfile(@RequestHeader("X-User-Id") String userId) {
        return userProfileService.getUserProfile(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfile> updateProfile(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody UpdateProfileRequest request) {

        UserProfile updatedProfile = userProfileService.updateProfile(
                userId,
                request.getFirstName(),
                request.getLastName(),
                request.getPhoneNumber()
        );
        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/me/addresses")
    public ResponseEntity<List<String>> getMyAddresses(@RequestHeader("X-User-Id") String userId) {
        return userProfileService.getUserProfile(userId)
                .map(profile -> ResponseEntity.ok(profile.getAddresses()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me/addresses")
    public ResponseEntity<UserProfile> addAddress(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody AddAddressRequest request) {

        UserProfile updatedProfile = userProfileService.addAddress(userId, request.getAddress());
        return ResponseEntity.ok(updatedProfile);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createUserProfile(@RequestBody CreateUserRequest request) {
        System.out.println("Creating user profile for: " + request.getUserId());

        userProfileService.createOrUpdateProfile(request.getUserId(), request.getEmail());
        return ResponseEntity.ok().build();
    }

}
