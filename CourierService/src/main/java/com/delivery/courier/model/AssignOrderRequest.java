package com.delivery.courier.model;

public class AssignOrderRequest {
    private String deliveryAddress;
    private String restaurantAddress;

    public AssignOrderRequest() {}

    public AssignOrderRequest(String deliveryAddress, String restaurantAddress) {
        this.deliveryAddress = deliveryAddress;
        this.restaurantAddress = restaurantAddress;
    }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getRestaurantAddress() { return restaurantAddress; }
    public void setRestaurantAddress(String restaurantAddress) { this.restaurantAddress = restaurantAddress; }
}