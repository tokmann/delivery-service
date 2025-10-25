package com.delivery.restaurant.model;

public class UpdateCatalogRequest {
    private String action;
    private String restaurantId;

    public UpdateCatalogRequest() {}

    public UpdateCatalogRequest(String action, String restaurantId) {
        this.action = action;
        this.restaurantId = restaurantId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
