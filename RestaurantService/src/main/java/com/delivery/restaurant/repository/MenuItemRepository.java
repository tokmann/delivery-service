package com.delivery.restaurant.repository;

import com.delivery.restaurant.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, String> {

    // Найти все блюда ресторана
    List<MenuItem> findByRestaurantId(String restaurantId);
    // Найти блюдо по ID и ресторану
    Optional<MenuItem> findByIdAndRestaurantId(String id, String restaurantId);

}