package com.delivery.courier.repository;

import com.delivery.courier.model.DeliveryAssignment;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryAssignmentRepository extends JpaRepository<DeliveryAssignment, String> {
    Optional<DeliveryAssignment> findByIdAndCourierId(String id, String courierId);
    Optional<DeliveryAssignment> findByOrderId(String orderId);
    List<DeliveryAssignment> findByCourierId(String courierId);
}