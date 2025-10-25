package com.delivery.restaurant.dto;

public class SyncRestaurantDTO {
    private String id;
    private String name;
    private String address;
    private String cuisineType;
    private String managerId;
    private boolean isActive;

    public SyncRestaurantDTO() {}

    public SyncRestaurantDTO(String id, String name, String address, String cuisineType,
                             String managerId, boolean isActive) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.cuisineType = cuisineType;
        this.managerId = managerId;
        this.isActive = isActive;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCuisineType() { return cuisineType; }
    public void setCuisineType(String cuisineType) { this.cuisineType = cuisineType; }

    public String getManagerId() { return managerId; }
    public void setManagerId(String managerId) { this.managerId = managerId; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}