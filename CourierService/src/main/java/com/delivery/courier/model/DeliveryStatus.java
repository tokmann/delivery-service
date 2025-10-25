package com.delivery.courier.model;

public class DeliveryStatus {
    private String orderId;
    private String status;
    private String courierName;
    private String estimatedTime;

    public DeliveryStatus() {}

    public DeliveryStatus(String orderId, String status, String courierName, String estimatedTime) {
        this.orderId = orderId;
        this.status = status;
        this.courierName = courierName;
        this.estimatedTime = estimatedTime;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCourierName() { return courierName; }
    public void setCourierName(String courierName) { this.courierName = courierName; }

    public String getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(String estimatedTime) { this.estimatedTime = estimatedTime; }
}