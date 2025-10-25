package com.delivery.catalog.model;

public class UpdateCatalogRequest {
    private String restaurantId;
    private String action; // CREATE, UPDATE, DELETE

    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
}