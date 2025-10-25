package com.delivery.auth.dto;

public class TokenValidationResponse {
    private boolean isValid;
    private String userId;
    private String login;
    private String role;

    public TokenValidationResponse(boolean isValid, String userId, String login, String role) {
        this.isValid = isValid;
        this.userId = userId;
        this.login = login;
        this.role = role;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}