package com.delivery.catalog.controller;

import com.delivery.catalog.dto.CatalogMenuItemDTO;
import com.delivery.catalog.dto.CatalogRestaurantDTO;
import com.delivery.catalog.model.UpdateCatalogRequest;
import com.delivery.catalog.service.CatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/menu-items")
    public List<CatalogMenuItemDTO> getAllMenuItems() {
        return catalogService.getAllMenuItems();
    }

    @GetMapping("/restaurants/{restaurantId}/menu-items")
    public List<CatalogMenuItemDTO> getMenuItemsByRestaurant(@PathVariable String restaurantId) {
        return catalogService.getMenuItemsByRestaurant(restaurantId);
    }

    @GetMapping("/restaurants")
    public List<CatalogRestaurantDTO> getAllRestaurants() {
        return catalogService.getAllRestaurants();
    }

    @GetMapping("/restaurants/{restaurantId}")
    public CatalogRestaurantDTO getRestaurant(@PathVariable String restaurantId) {
        return catalogService.getRestaurantById(restaurantId);
    }

    @PostMapping("/internal/sync")
    public ResponseEntity<Void> syncCatalog(@RequestBody UpdateCatalogRequest request) {
        catalogService.handleCatalogUpdate(request);
        return ResponseEntity.ok().build();
    }
}
