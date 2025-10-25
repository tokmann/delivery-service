package com.delivery.catalog.service;

import com.delivery.catalog.dto.CatalogMenuItemDTO;
import com.delivery.catalog.dto.CatalogRestaurantDTO;
import com.delivery.catalog.dto.SyncMenuItemDTO;
import com.delivery.catalog.dto.SyncRestaurantDTO;
import com.delivery.catalog.model.CatalogMenuItem;
import com.delivery.catalog.model.CatalogRestaurant;
import com.delivery.catalog.model.UpdateCatalogRequest;
import com.delivery.catalog.repository.CatalogMenuItemRepository;
import com.delivery.catalog.repository.CatalogRestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {

    private final CatalogRestaurantRepository restaurantRepository;
    private final CatalogMenuItemRepository menuItemRepository;
    private final WebClient webClient;

    public CatalogService(CatalogRestaurantRepository restaurantRepository,
                          CatalogMenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.webClient = WebClient.builder()
                .baseUrl("http://restaurant-service:8083/api/management/restaurants/internal")
                .build();
    }

    // === Внутренние методы синхронизации ===
    public void handleCatalogUpdate(UpdateCatalogRequest request) {
        switch (request.getAction()) {
            case "CREATE", "UPDATE" -> syncRestaurantData(request.getRestaurantId());
            case "DELETE" -> deactivateRestaurant(request.getRestaurantId());
            case "MENU_UPDATE" -> syncRestaurantMenu(request.getRestaurantId());
            default -> System.out.println("Unknown action: " + request.getAction());
        }
    }

    private void syncRestaurantData(String restaurantId) {
        webClient.get()
                .uri("/restaurants/{restaurantId}", restaurantId)
                .retrieve()
                .bodyToMono(SyncRestaurantDTO.class) // Исправлено
                .subscribe(restaurantDTO -> {
                    CatalogRestaurant catalogRestaurant = CatalogRestaurant.fromRestaurant(restaurantDTO);
                    catalogRestaurant.setActive(true);
                    restaurantRepository.save(catalogRestaurant);
                    syncRestaurantMenu(restaurantId);
                });
    }

    private void syncRestaurantMenu(String restaurantId) {
        webClient.get()
                .uri("/restaurants/{restaurantId}/menu-items", restaurantId)
                .header("X-User-Id", "catalog-service")
                .retrieve()
                .bodyToFlux(SyncMenuItemDTO.class)
                .collectList()
                .subscribe(menuItemDTOs -> {
                    CatalogRestaurant restaurant = restaurantRepository.findById(restaurantId)
                            .orElseThrow(() -> new RuntimeException("Restaurant not found"));

                    List<CatalogMenuItem> catalogMenuItems = menuItemDTOs.stream()
                            .map(dto -> CatalogMenuItem.fromMenuItem(dto, restaurant)) // Исправлено
                            .collect(Collectors.toList());

                    menuItemRepository.deleteByRestaurantId(restaurantId);
                    menuItemRepository.saveAll(catalogMenuItems);

                    System.out.println("Synced " + catalogMenuItems.size() + " menu items for restaurant: " + restaurantId);
                });
    }

    private void deactivateRestaurant(String restaurantId) {
        restaurantRepository.findById(restaurantId).ifPresent(r -> {
            r.setActive(false);
            restaurantRepository.save(r);
        });
    }

    public List<CatalogMenuItemDTO> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .map(CatalogMenuItem::toDTO)
                .collect(Collectors.toList());
    }

    public List<CatalogMenuItemDTO> getMenuItemsByRestaurant(String restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId).stream()
                .map(CatalogMenuItem::toDTO)
                .collect(Collectors.toList());
    }

    public List<CatalogRestaurantDTO> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(CatalogRestaurant::toDTO)
                .collect(Collectors.toList());
    }

    public CatalogRestaurantDTO getRestaurantById(String restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .map(CatalogRestaurant::toDTO)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }
}