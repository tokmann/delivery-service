package com.delivery.order.dto;

import java.math.BigDecimal;

public class CatalogMenuItemDTO {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private boolean isAvailable;
    private String restaurantId;

    public CatalogMenuItemDTO() {}

    public CatalogMenuItemDTO(String id, String name, String description, BigDecimal price,
                              String category, boolean isAvailable, String restaurantId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.isAvailable = isAvailable;
        this.restaurantId = restaurantId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }
}