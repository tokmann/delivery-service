package com.delivery.auth.dto;

public class AuthResponse {
    private String userId;
    private String accessToken;
    private String tokenType = "Bearer";
    private String role;

    public AuthResponse(String userId, String accessToken, String role) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.role = role;
    }

    public String getRoles() {
        return role;
    }

    public void setRoles(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
