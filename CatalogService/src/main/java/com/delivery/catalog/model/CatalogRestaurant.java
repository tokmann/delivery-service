package com.delivery.catalog.model;

import com.delivery.catalog.dto.CatalogRestaurantDTO;
import com.delivery.catalog.dto.SyncRestaurantDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "catalog_restaurants")
public class CatalogRestaurant {
    @Id
    private String id;

    private String name;
    private String address;
    private String cuisineType;
    private Double rating;
    private String phoneNumber;
    private String openingHours;
    private boolean isActive;

    public CatalogRestaurantDTO toDTO() {
        return new CatalogRestaurantDTO(
                this.id,
                this.name,
                this.address,
                this.cuisineType,
                this.rating,
                this.phoneNumber,
                this.openingHours,
                this.isActive
        );
    }

    public static CatalogRestaurant fromRestaurant(SyncRestaurantDTO restaurant) {
        CatalogRestaurant catalogRestaurant = new CatalogRestaurant();
        catalogRestaurant.setId(restaurant.getId());
        catalogRestaurant.setName(restaurant.getName());
        catalogRestaurant.setAddress(restaurant.getAddress());
        catalogRestaurant.setCuisineType(restaurant.getCuisineType());
        catalogRestaurant.setActive(restaurant.isActive());
        catalogRestaurant.setRating(0.0);
        catalogRestaurant.setPhoneNumber("");
        catalogRestaurant.setOpeningHours("");
        return catalogRestaurant;
    }

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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }
}