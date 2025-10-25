package com.delivery.restaurant.repository;

import com.delivery.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, String> {

    // Найти активные рестораны по менеджеру
    List<Restaurant> findByManagerIdAndIsActiveTrue(String managerId);

    // Проверить существует ли ресторан с таким менеджером
    boolean existsByIdAndManagerId(String restaurantId, String managerId);

    // Найти активный ресторан по ID
    Optional<Restaurant> findByIdAndIsActiveTrue(String id);

    // Найти по id ресторана с проверкой на права менеджера
    Optional<Restaurant> findByIdAndManagerId(String restaurantId, String managerId);
}