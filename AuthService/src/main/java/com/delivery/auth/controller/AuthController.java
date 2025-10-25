package com.delivery.auth.controller;

import com.delivery.auth.dto.AuthRequest;
import com.delivery.auth.dto.AuthResponse;
import com.delivery.auth.dto.RegisterRequest;
import com.delivery.auth.dto.TokenValidationResponse;
import com.delivery.auth.service.AuthService;
import com.delivery.auth.service.UserService;
import com.delivery.auth.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Set;

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

