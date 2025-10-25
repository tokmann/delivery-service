package com.delivery.gateway.config;

import com.delivery.gateway.filter.AuthFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GatewayConfig {

    @Bean
    @ConfigurationProperties(prefix = "auth.filter")
    public AuthFilter.Config authFilterConfig() {
        return new AuthFilter.Config();
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
