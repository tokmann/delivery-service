package com.delivery.courier.controller;

import com.delivery.courier.model.*;
import com.delivery.courier.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courier")
public class CourierController {

    @Autowired
    private CourierService courierService;

    // Для курьеров - получить мои задания
    @GetMapping("/assignments")
    public ResponseEntity<List<DeliveryAssignment>> getCourierAssignments(
            @RequestHeader("X-User-Id") String courierId) {
        List<DeliveryAssignment> assignments = courierService.getCourierAssignments(courierId);
        return ResponseEntity.ok(assignments);
    }

    // Для курьеров - обновить статус доставки
    @PutMapping("/assignments/{assignmentId}/status")
    public ResponseEntity<?> updateDeliveryStatus(
            @RequestHeader("X-User-Id") String courierId,
            @PathVariable String assignmentId,
            @RequestBody UpdateDeliveryStatusRequest request) {

        Optional<DeliveryAssignment> updatedAssignment =
                courierService.updateDeliveryStatus(assignmentId, courierId, request.getStatus());

        return updatedAssignment
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Для клиентов - отследить заказ
    @GetMapping("/orders/{orderId}/track")
    public ResponseEntity<DeliveryStatus> trackOrder(@PathVariable String orderId) {
        Optional<DeliveryStatus> deliveryStatus = courierService.trackOrder(orderId);
        return deliveryStatus
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Внутренний эндпоинт - назначить заказ на доставку
    @PostMapping("/internal/orders/{orderId}/assign")
    public ResponseEntity<DeliveryAssignment> assignOrder(
            @PathVariable String orderId,
            @RequestBody AssignOrderRequest request) {

        DeliveryAssignment assignment = courierService.assignOrder(orderId, request);
        return ResponseEntity.status(201).body(assignment);
    }
}