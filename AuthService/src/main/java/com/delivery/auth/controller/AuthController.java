package com.delivery.auth.controller;

import com.delivery.auth.dto.*;
import com.delivery.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerCustomer(request));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponse> registerAdmin(
            @RequestHeader("Authorization") String token,
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerAdmin(token, request));
    }

    @PostMapping("/register/manager")
    public ResponseEntity<AuthResponse> registerManager(
            @RequestHeader("Authorization") String token,
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerManager(token, request));
    }

    @PostMapping("/register/courier")
    public ResponseEntity<String> registerCourier(
            @RequestHeader("Authorization") String token,
            @RequestBody RegisterCourierRequest request) {
        authService.registerCourier(token, request);
        return ResponseEntity.ok("Courier is created: " + request.getName() + " " + request.getPhoneNumber());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(authService.validate(authHeader));
    }
}

