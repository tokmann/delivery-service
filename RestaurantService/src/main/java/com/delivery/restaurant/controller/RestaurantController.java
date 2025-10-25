package com.delivery.restaurant.controller;

import com.delivery.restaurant.model.*;
import com.delivery.restaurant.repository.MenuItemRepository;
import com.delivery.restaurant.service.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/management/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final MenuItemRepository menuItemRepository;

    public RestaurantController(RestaurantService restaurantService, MenuItemRepository menuItemRepository) {
        this.restaurantService = restaurantService;
        this.menuItemRepository = menuItemRepository;
    }

    // СОЗДАНИЕ РЕСТОРАНА (только ADMIN)
    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(
            @RequestHeader("X-User-Role") String userRoles,
            @RequestBody CreateRestaurantRequest request) {

        if (!userRoles.contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Restaurant restaurant = restaurantService.createRestaurant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurant);
    }

    // ПОЛУЧЕНИЕ МОИХ РЕСТОРАНОВ (для MANAGER)
    @GetMapping
    public ResponseEntity<List<Restaurant>> getMyRestaurants(
            @RequestHeader("X-User-Id") String userId) {

        List<Restaurant> restaurants = restaurantService.getManagerRestaurants(userId);
        return ResponseEntity.ok(restaurants);
    }

    // ОБНОВЛЕНИЕ РЕСТОРАНА
    @PutMapping("/{restaurantId}")
    public ResponseEntity<Restaurant> updateRestaurant(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String restaurantId,
            @RequestBody UpdateRestaurantRequest request) {

        Restaurant updatedRestaurant = restaurantService.updateRestaurant(
                restaurantId, userId, request.getName(), request.getAddress());

        return ResponseEntity.ok(updatedRestaurant);
    }

    // ДОБАВЛЕНИЕ БЛЮДА В МЕНЮ
    @PostMapping("/{restaurantId}/menu-items")
    public ResponseEntity<MenuItem> addMenuItem(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String restaurantId,
            @RequestBody CreateMenuItemRequest request) {

        MenuItem menuItem = restaurantService.addMenuItem(restaurantId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItem);
    }

    // ПОЛУЧЕНИЕ МЕНЮ РЕСТОРАНА
    @GetMapping("/{restaurantId}/menu-items")
    public ResponseEntity<List<MenuItem>> getRestaurantMenu(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String restaurantId) {

        List<MenuItem> menuItems = restaurantService.getRestaurantMenu(restaurantId, userId);
        return ResponseEntity.ok(menuItems);
    }

    // ОБНОВЛЕНИЕ БЛЮДА
    @PutMapping("/{restaurantId}/menu-items/{itemId}")
    public ResponseEntity<MenuItem> updateMenuItem(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String restaurantId,
            @PathVariable String itemId,
            @RequestBody UpdateMenuItemRequest request) {

        MenuItem updatedMenuItem = restaurantService.updateMenuItem(
                restaurantId, itemId, userId, request);

        return ResponseEntity.ok(updatedMenuItem);
    }

    // УДАЛЕНИЕ БЛЮДА
    @DeleteMapping("/{restaurantId}/menu-items/{itemId}")
    public ResponseEntity<Void> deleteMenuItem(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String restaurantId,
            @PathVariable String itemId) {

        restaurantService.deleteMenuItem(restaurantId, itemId, userId);
        return ResponseEntity.ok().build();
    }
}