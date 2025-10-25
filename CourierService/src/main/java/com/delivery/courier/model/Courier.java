package com.delivery.courier.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "couriers")
public class Courier {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String status = "AVAILABLE"; // AVAILABLE, BUSY

    private LocalDateTime busyUntil; // Когда курьер освободится

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Конструкторы
    public Courier() {}

    public Courier(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.createdAt = LocalDateTime.now();
    }

    // Геттеры и сеттеры
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getBusyUntil() { return busyUntil; }
    public void setBusyUntil(LocalDateTime busyUntil) { this.busyUntil = busyUntil; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Метод для проверки доступности
    public boolean isAvailable() {
        return "AVAILABLE".equals(status) &&
                (busyUntil == null || LocalDateTime.now().isAfter(busyUntil));
    }

    // Метод для занятия курьера на 30 минут
    public void makeBusy() {
        this.status = "BUSY";
        this.busyUntil = LocalDateTime.now().plusMinutes(30);
        this.updatedAt = LocalDateTime.now();
    }

    // Метод для освобождения курьера
    public void makeAvailable() {
        this.status = "AVAILABLE";
        this.busyUntil = null;
        this.updatedAt = LocalDateTime.now();
    }
}