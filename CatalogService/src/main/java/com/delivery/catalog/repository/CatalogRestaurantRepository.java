package com.delivery.catalog.repository;

import com.delivery.catalog.model.CatalogRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatalogRestaurantRepository extends JpaRepository<CatalogRestaurant, String> {

    Optional<CatalogRestaurant> findByIdAndIsActiveTrue(String id);

    List<CatalogRestaurant> findByCuisineTypeAndIsActiveTrue(String cuisineType);

    @Query("SELECT r FROM CatalogRestaurant r WHERE r.isActive = true AND " +
            "(LOWER(r.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(r.address) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<CatalogRestaurant> searchActiveRestaurants(@Param("searchTerm") String searchTerm);

    Optional<CatalogRestaurant> findById(String id);

    List<CatalogRestaurant> findByIsActiveTrue();
}