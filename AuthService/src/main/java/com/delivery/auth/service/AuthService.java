package com.delivery.auth.service;

import com.delivery.auth.config.UserProfileClient;
import com.delivery.auth.dto.AuthRequest;
import com.delivery.auth.dto.AuthResponse;
import com.delivery.auth.dto.RegisterRequest;
import com.delivery.auth.model.Role;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.delivery.auth.dto.TokenValidationResponse;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final UserProfileClient userProfileClient;

    public AuthService(UserService userService, JwtService jwtService, UserProfileClient userProfileClient) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.userProfileClient = userProfileClient;
    }

    public AuthResponse registerCustomer(RegisterRequest request) {
        var user = userService.registerUser(request.getLogin(), request.getPassword(), request.getEmail(), Role.CUSTOMER.name());
        var token = jwtService.generateToken(user.getId(), user.getLogin(), user.getRole());
        userProfileClient.createProfile(user.getId(), user.getEmail());
        return new AuthResponse(user.getId(), token, user.getRole());
    }

    public AuthResponse registerAdmin(String token, RegisterRequest request) {
        String caller = jwtService.getRolesFromToken(token);
        if (!"ADMIN".equals(caller)) throw new AccessDeniedException("Only admin can create admins");
        var user = userService.registerUser(request.getLogin(), request.getPassword(), request.getEmail(), request.getRole());
        return new AuthResponse(user.getId(), jwtService.generateToken(user.getId(), user.getLogin(), user.getRole()), user.getRole());
    }

    public AuthResponse registerManager(String token, RegisterRequest request) {
        String caller = jwtService.getRolesFromToken(token);
        if (!"ADMIN".equals(caller)) throw new AccessDeniedException("Only admin can create managers");
        var user = userService.registerUser(request.getLogin(), request.getPassword(), request.getEmail(), Role.RESTAURANT_MANAGER.name());
        return new AuthResponse(user.getId(), jwtService.generateToken(user.getId(), user.getLogin(), user.getRole()), user.getRole());
    }

    public AuthResponse login(AuthRequest request) {
        var user = userService.authenticate(request.getLogin(), request.getPassword());
        var token = jwtService.generateToken(user.getId(), user.getLogin(), user.getRole());
        return new AuthResponse(user.getId(), token, user.getRole());
    }

    public TokenValidationResponse validate(String authHeader) {
        var token = authHeader.replace("Bearer ", "");
        if (!jwtService.validateToken(token)) return new TokenValidationResponse(false, null, null, null);
        return new TokenValidationResponse(true,
                jwtService.getUserIdFromToken(token),
                jwtService.getLoginFromToken(token),
                jwtService.getRolesFromToken(token));
    }
}

