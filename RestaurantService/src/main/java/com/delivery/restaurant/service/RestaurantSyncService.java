package com.delivery.restaurant.service;

import com.delivery.restaurant.model.UpdateCatalogRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RestaurantSyncService {

    private final WebClient webClient;

    public RestaurantSyncService() {
        this.webClient = WebClient.builder()
                .baseUrl("http://catalog-service:8084/api/catalog/internal")
                .build();
    }

    public void sendCatalogUpdate(String action, String restaurantId) {
        UpdateCatalogRequest request = new UpdateCatalogRequest(action, restaurantId);
        webClient.post()
                .uri("/sync")
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }
}
