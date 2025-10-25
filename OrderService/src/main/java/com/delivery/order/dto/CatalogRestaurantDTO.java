package com.delivery.order.dto;

public class CatalogRestaurantDTO {
    private String id;
    private String name;
    private String address;
    private String cuisineType;
    private Double rating;
    private String phoneNumber;
    private String openingHours;
    private boolean isActive;

    public CatalogRestaurantDTO() {}

    public CatalogRestaurantDTO(String id, String name, String address, String cuisineType,
                                Double rating, String phoneNumber, String openingHours, boolean isActive) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.cuisineType = cuisineType;
        this.rating = rating;
        this.phoneNumber = phoneNumber;
        this.openingHours = openingHours;
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

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getOpeningHours() { return openingHours; }
    public void setOpeningHours(String openingHours) { this.openingHours = openingHours; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}