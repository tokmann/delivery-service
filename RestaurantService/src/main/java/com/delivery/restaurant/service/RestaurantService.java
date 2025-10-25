package com.delivery.restaurant.service;

import com.delivery.restaurant.model.*;
import com.delivery.restaurant.repository.RestaurantRepository;
import com.delivery.restaurant.repository.MenuItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantSyncService syncService;

    public RestaurantService(RestaurantRepository restaurantRepository,
                             MenuItemRepository menuItemRepository,
                             RestaurantSyncService syncService) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.syncService = syncService;
    }

    // === 1. Создать ресторан (только ADMIN) ===
    public Restaurant createRestaurant(CreateRestaurantRequest request) {
        Restaurant restaurant = new Restaurant(
                request.getName(),
                request.getAddress(),
                request.getCuisineType(),
                request.getManagerId()
        );

        Restaurant saved = restaurantRepository.save(restaurant);
        syncService.sendCatalogUpdate("CREATE", saved.getId().toString());
        return saved;
    }

    // === 2. Получить рестораны менеджера ===
    public List<Restaurant> getManagerRestaurants(String managerId) {
        return restaurantRepository.findByManagerIdAndIsActiveTrue(managerId);
    }

    // === 3. Получить ресторан по ID (с проверкой прав менеджера) ===
    public Restaurant getRestaurantForManager(String restaurantId, String managerId) {
        return restaurantRepository.findByIdAndManagerId(restaurantId, managerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Restaurant not found or access denied"));
    }

    // === 4. Обновить ресторан ===
    public Restaurant updateRestaurant(String restaurantId, String managerId,
                                       String name, String address) {
        Restaurant restaurant = getRestaurantForManager(restaurantId, managerId);

        if (name != null && !name.isBlank()) restaurant.setName(name);
        if (address != null && !address.isBlank()) restaurant.setAddress(address);

        Restaurant updated = restaurantRepository.save(restaurant);
        syncService.sendCatalogUpdate("UPDATE", updated.getId().toString());
        return updated;
    }

    // === 5. Получить ресторан для CatalogService ===
    public Restaurant getRestaurantForCatalog(String restaurantId) {
        return restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Restaurant not found"));
    }

    // === 6. Добавить блюдо в меню ===
    public MenuItem addMenuItem(String restaurantId, String managerId,
                                CreateMenuItemRequest request) {
        Restaurant restaurant = getRestaurantForManager(restaurantId, managerId);

        MenuItem menuItem = new MenuItem(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getCategory(),
                restaurant
        );

        MenuItem saved = menuItemRepository.save(menuItem);
        syncService.sendCatalogUpdate("MENU_UPDATE", restaurantId);
        return saved;
    }

    // === 7. Получить меню ресторана ===
    public List<MenuItem> getRestaurantMenu(String restaurantId, String managerId) {
        getRestaurantForManager(restaurantId, managerId);
        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    // === 8. Обновить блюдо ===
    public MenuItem updateMenuItem(String restaurantId, String menuItemId,
                                   String managerId, UpdateMenuItemRequest request) {
        getRestaurantForManager(restaurantId, managerId);

        MenuItem menuItem = menuItemRepository.findByIdAndRestaurantId(menuItemId, restaurantId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Menu item not found"));

        if (request.getName() != null && !request.getName().isBlank())
            menuItem.setName(request.getName());
        if (request.getPrice() != null)
            menuItem.setPrice(request.getPrice());
        if (request.getIsAvailable() != null)
            menuItem.setAvailable(request.getIsAvailable());

        MenuItem updated = menuItemRepository.save(menuItem);
        syncService.sendCatalogUpdate("MENU_UPDATE", restaurantId);
        return updated;
    }

    // === 9. Удалить блюдо ===
    public void deleteMenuItem(String restaurantId, String menuItemId, String managerId) {
        getRestaurantForManager(restaurantId, managerId);

        MenuItem menuItem = menuItemRepository.findByIdAndRestaurantId(menuItemId, restaurantId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Menu item not found"));

        menuItemRepository.delete(menuItem);
        syncService.sendCatalogUpdate("MENU_UPDATE", restaurantId);
    }

    // === 10. Деактивировать ресторан (например при удалении) ===
    public void deactivateRestaurant(String restaurantId) {
        restaurantRepository.findById(restaurantId).ifPresent(restaurant -> {
            restaurant.setActive(false);
            restaurantRepository.save(restaurant);
            syncService.sendCatalogUpdate("DELETE", restaurantId);
        });
    }

    // === 11. Получить меню ресторана для внутреннего использования ===
    public List<MenuItem> getRestaurantMenuInternal(String restaurantId) {
        // Для внутренних запросов не проверяем права менеджера
        return menuItemRepository.findByRestaurantId(restaurantId);
    }
}
