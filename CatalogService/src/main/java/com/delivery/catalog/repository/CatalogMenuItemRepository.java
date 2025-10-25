package com.delivery.catalog.repository;

import com.delivery.catalog.model.CatalogMenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogMenuItemRepository extends JpaRepository<CatalogMenuItem, String> {
    List<CatalogMenuItem> findByRestaurantId(String restaurantId);
    void deleteByRestaurantId(String restaurantId);
}