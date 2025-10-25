package com.delivery.courier.repository;

import com.delivery.courier.model.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourierRepository extends JpaRepository<Courier, String> {

    List<Courier> findByStatus(String status);

    @Query("SELECT c FROM Courier c WHERE c.status = 'AVAILABLE' OR (c.busyUntil IS NOT NULL AND c.busyUntil < CURRENT_TIMESTAMP)")
    List<Courier> findAvailableCouriers();

    Optional<Courier> findByName(String name);
}