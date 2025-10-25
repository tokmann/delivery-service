package com.delivery.order.controller;

import com.delivery.order.model.*;
import com.delivery.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody CreateOrderRequest request) {
        try {
            Order order = orderService.createOrder(userId, request);
            return ResponseEntity.status(201).body(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> getUserOrders(@RequestHeader("X-User-Id") String userId) {
        List<Order> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String orderId) {
        Optional<Order> order = orderService.getOrder(orderId);

        if (order.isPresent() && order.get().getUserId().equals(userId)) {
            return ResponseEntity.ok(order.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @RequestHeader("X-User-Role") String userRole,
            @PathVariable String orderId,
            @RequestBody UpdateOrderStatusRequest request) {

        if (!userRole.equals("RESTAURANT") && !userRole.equals("COURIER")) {
            return ResponseEntity.status(403).body("Недостаточно прав");
        }

        Optional<Order> updatedOrder = orderService.updateOrderStatus(orderId, request.getStatus());
        return updatedOrder.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Внутренний эндпоинт для Courier Service
    @GetMapping("/internal/{orderId}")
    public ResponseEntity<Order> getOrderInternal(@PathVariable String orderId) {
        Optional<Order> order = orderService.getOrder(orderId);
        return order.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Эндпоинт для ресторана
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Order>> getRestaurantOrders(
            @RequestHeader("X-User-Role") String userRole,
            @PathVariable String restaurantId) {

        if (!userRole.equals("RESTAURANT")) {
            return ResponseEntity.status(403).build();
        }

        List<Order> orders = orderService.getRestaurantOrders(restaurantId);
        return ResponseEntity.ok(orders);
    }
}