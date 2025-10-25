package com.delivery.catalog.model;

import com.delivery.catalog.dto.CatalogMenuItemDTO;
import com.delivery.catalog.dto.SyncMenuItemDTO;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "catalog_menu_items")
public class CatalogMenuItem {
    @Id
    private String id;

    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private boolean isAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private CatalogRestaurant restaurant;

    public CatalogMenuItemDTO toDTO() {
        return new CatalogMenuItemDTO(
                this.id,
                this.name,
                this.description,
                this.price,
                this.category,
                this.isAvailable,
                this.restaurant != null ? this.restaurant.getId() : null
        );
    }

    public static CatalogMenuItem fromMenuItem(SyncMenuItemDTO menuItem, CatalogRestaurant restaurant) {
        CatalogMenuItem catalogMenuItem = new CatalogMenuItem();
        catalogMenuItem.setId(menuItem.getId());
        catalogMenuItem.setName(menuItem.getName());
        catalogMenuItem.setDescription(menuItem.getDescription());
        catalogMenuItem.setPrice(menuItem.getPrice());
        catalogMenuItem.setCategory(menuItem.getCategory());
        catalogMenuItem.setAvailable(menuItem.isAvailable());
        catalogMenuItem.setRestaurant(restaurant);
        return catalogMenuItem;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public CatalogRestaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(CatalogRestaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}