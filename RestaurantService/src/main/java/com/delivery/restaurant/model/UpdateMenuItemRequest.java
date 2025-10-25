package com.delivery.restaurant.model;

import java.math.BigDecimal;

public class UpdateMenuItemRequest {
    private String name;
    private BigDecimal price;
    private Boolean isAvailable;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean available) { isAvailable = available; }
}