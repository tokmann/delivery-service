package com.delivery.order.service;

import com.delivery.order.dto.CatalogMenuItemDTO;
import com.delivery.order.dto.CatalogRestaurantDTO;
import com.delivery.order.model.*;
import com.delivery.order.repository.OrderRepository;
import com.delivery.order.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;

    @Value("${services.catalog.url:http://localhost:8084}")
    private String catalogServiceUrl;

    @Value("${services.courier.url:http://localhost:8086}")
    private String courierServiceUrl;

    public OrderService(RestTemplate restTemplate, OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    public Order createOrder(String userId, CreateOrderRequest request) {
        ResponseEntity<CatalogRestaurantDTO> restaurantResponse = restTemplate.getForEntity(
                catalogServiceUrl + "/api/catalog/restaurants/{restaurantId}",
                CatalogRestaurantDTO.class,
                request.getRestaurantId()
        );

        if (!restaurantResponse.getStatusCode().is2xxSuccessful() || restaurantResponse.getBody() == null) {
            throw new RuntimeException("Ресторан не найден: " + request.getRestaurantId());
        }

        CatalogRestaurantDTO restaurant = restaurantResponse.getBody();
        if (!restaurant.isActive()) {
            throw new RuntimeException("Ресторан временно недоступен");
        }

        ResponseEntity<CatalogMenuItemDTO[]> menuResponse = restTemplate.getForEntity(
                catalogServiceUrl + "/api/catalog/restaurants/{restaurantId}/menu-items",
                CatalogMenuItemDTO[].class,
                request.getRestaurantId()
        );

        if (!menuResponse.getStatusCode().is2xxSuccessful() || menuResponse.getBody() == null) {
            throw new RuntimeException("Не удалось получить меню ресторана");
        }

        List<CatalogMenuItemDTO> menuItems = List.of(menuResponse.getBody());

        for (CreateOrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            CatalogMenuItemDTO menuItem = menuItems.stream()
                    .filter(mi -> mi.getId().equals(itemRequest.getMenuItemId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Блюдо не найдено: " + itemRequest.getMenuItemId()));

            if (!menuItem.isAvailable()) {
                throw new RuntimeException("Блюдо недоступно: " + menuItem.getName());
            }
        }

        Order order = new Order(
                userId,
                restaurant.getAddress(),
                request.getRestaurantId(),
                request.getDeliveryAddress()
        );

        for (CreateOrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            CatalogMenuItemDTO menuItem = menuItems.stream()
                    .filter(mi -> mi.getId().equals(itemRequest.getMenuItemId()))
                    .findFirst()
                    .get();

            OrderItem orderItem = new OrderItem(
                    menuItem.getId(),
                    menuItem.getName(),
                    menuItem.getPrice(),
                    itemRequest.getQuantity(),
                    order
            );
            order.getItems().add(orderItem);
        }

        order.calculateTotalPrice();

        Order savedOrder = orderRepository.save(order);

        try {
            AssignOrderRequest assignRequest = new AssignOrderRequest(
                    savedOrder.getDeliveryAddress(),
                    savedOrder.getRestaurantAddress()
            );

            restTemplate.postForEntity(
                    courierServiceUrl + "/api/courier/internal/orders/{orderId}/assign",
                    assignRequest,
                    Void.class,
                    savedOrder.getId()
            );

        } catch (Exception e) {
            System.out.println("Не удалось назначить курьера: " + e.getMessage());
        }

        return savedOrder;
    }

    public List<Order> getUserOrders(String userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Optional<Order> getOrder(String orderId) {
        return orderRepository.findById(orderId);
    }

    public Optional<Order> updateOrderStatus(String orderId, String status) {
        return orderRepository.findById(orderId).map(order -> {
            order.setStatus(status);
            order.setUpdatedAt(java.time.LocalDateTime.now());
            return orderRepository.save(order);
        });
    }

    public List<Order> getRestaurantOrders(String restaurantId) {
        return orderRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId);
    }
}