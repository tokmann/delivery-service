package com.delivery.courier.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_assignments")
public class DeliveryAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id", nullable = false)
    private Courier courier;

    @Column(nullable = false)
    private String deliveryAddress;

    @Column(nullable = false)
    private String restaurantAddress;

    @Column(nullable = false)
    private String status = "ASSIGNED"; // ASSIGNED, PICKED_UP, ON_THE_WAY, DELIVERED

    private LocalDateTime assignedAt;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime pickedUpAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime updatedAt;
    private String courierId;

    // Конструкторы, геттеры, сеттеры
    public DeliveryAssignment() {}

    public DeliveryAssignment(String orderId, Courier courier, String deliveryAddress, String restaurantAddress) {
        this.orderId = orderId;
        this.courier = courier;
        this.deliveryAddress = deliveryAddress;
        this.restaurantAddress = restaurantAddress;
        this.assignedAt = LocalDateTime.now();
        this.estimatedDeliveryTime = LocalDateTime.now().plusMinutes(45);
    }

    // Геттеры и сеттеры...
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public Courier getCourier() { return courier; }
    public void setCourier(Courier courier) { this.courier = courier; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getRestaurantAddress() { return restaurantAddress; }
    public void setRestaurantAddress(String restaurantAddress) { this.restaurantAddress = restaurantAddress; }

    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    public LocalDateTime getEstimatedDeliveryTime() { return estimatedDeliveryTime; }
    public void setEstimatedDeliveryTime(LocalDateTime estimatedDeliveryTime) { this.estimatedDeliveryTime = estimatedDeliveryTime; }

    public LocalDateTime getPickedUpAt() { return pickedUpAt; }
    public void setPickedUpAt(LocalDateTime pickedUpAt) { this.pickedUpAt = pickedUpAt; }

    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }

    public String getCourierId() {
        return courierId;
    }

    public void setCourierId(String courierId) {
        this.courierId = courierId;
    }

    // Дополнительные методы
    public void markAsPickedUp() {
        this.status = "PICKED_UP";
        this.pickedUpAt = LocalDateTime.now();
    }

    public void markAsDelivered() {
        this.status = "DELIVERED";
        this.deliveredAt = LocalDateTime.now();
        // Освобождаем курьера
        if (this.courier != null) {
            this.courier.makeAvailable();
        }
    }
}