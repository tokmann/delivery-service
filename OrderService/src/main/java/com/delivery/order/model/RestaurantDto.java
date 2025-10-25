package com.delivery.order.model;

import jakarta.persistence.Id;

public class RestaurantDto {

    private String id;
    private String name;
    private String address;
    private String cuisineType;
    private Double rating;
    private String phoneNumber;
    private String openingHours;
    private boolean isActive;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public Double getRating() {
        return rating;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
