package com.delivery.restaurant.controller;

import com.delivery.restaurant.dto.SyncMenuItemDTO;
import com.delivery.restaurant.dto.SyncRestaurantDTO;
import com.delivery.restaurant.model.MenuItem;
import com.delivery.restaurant.model.Restaurant;
import com.delivery.restaurant.service.RestaurantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/management/restaurants/internal")
public class InternalRestaurantController {

    private final RestaurantService restaurantService;

    public InternalRestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/restaurants/{restaurantId}")
    public SyncRestaurantDTO getRestaurantInternal(@PathVariable String restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurantForCatalog(restaurantId);
        return convertToSyncRestaurantDTO(restaurant);
    }

    @GetMapping("/restaurants/{restaurantId}/menu-items")
    public List<SyncMenuItemDTO> getMenuItemsInternal(@PathVariable String restaurantId,
                                                      @RequestHeader("X-User-Id") String userId) {
        restaurantService.getRestaurantForCatalog(restaurantId);

        List<MenuItem> menuItems = restaurantService.getRestaurantMenuInternal(restaurantId);
        return menuItems.stream()
                .map(this::convertToSyncMenuItemDTO)
                .collect(Collectors.toList());
    }

    private SyncRestaurantDTO convertToSyncRestaurantDTO(Restaurant restaurant) {
        return new SyncRestaurantDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getCuisineType(),
                restaurant.getManagerId(),
                restaurant.isActive()
        );
    }

    private SyncMenuItemDTO convertToSyncMenuItemDTO(MenuItem menuItem) {
        return new SyncMenuItemDTO(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.getCategory(),
                menuItem.isAvailable(),
                menuItem.getRestaurant().getId()
        );
    }
}