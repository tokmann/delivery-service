package com.delivery.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final WebClient webClient;
    private final Set<String> publicEndpoints = Set.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/validate",
            "/actuator/health"
    );

    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClient = webClientBuilder.baseUrl("http://auth-service:8081").build();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();

            System.out.println("=== GATEWAY DEBUG ===");
            System.out.println("Path: " + path);
            System.out.println("Headers: " + exchange.getRequest().getHeaders());

            if (isPublicEndpoint(path)) {
                System.out.println("DEBUG: Public endpoint, skipping auth");
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            System.out.println("DEBUG: Auth header: " + authHeader);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("DEBUG: Missing or invalid auth header");
                return unauthorized(exchange, "Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);
            System.out.println("DEBUG: Token extracted: " + token);

            return validateToken(token)
                    .flatMap(authResponse -> {
                        System.out.println("DEBUG: Auth response - valid: " + authResponse.isValid());
                        System.out.println("DEBUG: Auth response - userId: " + authResponse.getUserId());
                        System.out.println("DEBUG: Auth response - role: " + authResponse.getRole());

                        if (authResponse.isValid()) {
                            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                    .header("X-User-Id", authResponse.getUserId())
                                    .header("X-User-Login", authResponse.getLogin())
                                    .header("X-User-Role", authResponse.getRole())
                                    .build();

                            System.out.println("DEBUG: Headers added to request");
                            return chain.filter(exchange.mutate().request(modifiedRequest).build());
                        } else {
                            System.out.println("DEBUG: Token validation failed");
                            return unauthorized(exchange, "Invalid token");
                        }
                    })
                    .onErrorResume(e -> {
                        System.out.println("DEBUG: Auth service error: " + e.getMessage());
                        return unauthorized(exchange, "Token validation error: " + e.getMessage());
                    });
        };
    }

    private Mono<AuthResponse> validateToken(String token) {
        System.out.println("DEBUG: Calling Auth service at: http://auth-service:8081/api/auth/validate");

        return webClient.post()
                .uri("/api/auth/validate")
                .header("token", token)
                .retrieve()
                .onStatus(status -> status.isError(), response -> {
                    System.out.println("DEBUG: Auth service returned error: " + response.statusCode());
                    return response.bodyToMono(String.class)
                            .doOnNext(body -> System.out.println("DEBUG: Error body: " + body))
                            .then(Mono.error(new RuntimeException("Auth service error")));
                })
                .bodyToMono(AuthResponse.class)
                .doOnNext(response -> System.out.println("DEBUG: Raw auth response received"))
                .timeout(java.time.Duration.ofSeconds(5))
                .onErrorMap(e -> {
                    System.out.println("DEBUG: Auth service timeout: " + e.getMessage());
                    return new RuntimeException("Auth service unavailable");
                });
    }

    private boolean isPublicEndpoint(String path) {
        return publicEndpoints.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("X-Auth-Error", message);
        return exchange.getResponse().setComplete();
    }

    public static class Config {

        private boolean enabled = true;
        private int timeoutSeconds = 5;
        private List<String> additionalPublicEndpoints;

        public boolean isEnabled() { return enabled; }

        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public int getTimeoutSeconds() { return timeoutSeconds; }

        public void setTimeoutSeconds(int timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }

        public List<String> getAdditionalPublicEndpoints() { return additionalPublicEndpoints; }

        public void setAdditionalPublicEndpoints(List<String> additionalPublicEndpoints) {
            this.additionalPublicEndpoints = additionalPublicEndpoints;
        }
    }

    public static class AuthResponse {
        private boolean isValid;
        private String userId;
        private String login;
        private String role;

        public boolean isValid() { return isValid; }

        public void setValid(boolean valid) { isValid = valid; }

        public String getUserId() { return userId; }

        public void setUserId(String userId) { this.userId = userId; }

        public String getLogin() { return login; }

        public void setLogin(String login) { this.login = login; }

        public String getRole() { return role; }

        public void setRole(String role) { this.role = role; }
    }
}