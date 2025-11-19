package com.delivery.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class UserProfileClient {
    private final WebClient webClient;

    public UserProfileClient(@Value("${services.user-service.url}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public void createProfile(String userId, String email) {
        var body = Map.of("userId", userId, "email", email);
        webClient.post()
                .uri("/api/users/create")
                .bodyValue(body)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}

