package com.delivery.order.service;

import com.delivery.order.dto.CatalogMenuItemDTO;
import com.delivery.order.dto.CatalogRestaurantDTO;
import com.delivery.order.model.*;
import com.delivery.order.repository.OrderRepository;
import com.delivery.order.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final WebClient webClient;
    private final OrderRepository orderRepository;
    private final String catalogServiceUrl = "http://catalog-service:8084";
    private final String courierServiceUrl = "http://courier-service:8086";

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.webClient = WebClient.builder().build();
    }

    public Order createOrder(String userId, CreateOrderRequest request) {

        CatalogRestaurantDTO restaurant = webClient.get()
                .uri(catalogServiceUrl + "/api/catalog/restaurants/{restaurantId}", request.getRestaurantId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("Ресторан не найден: " + request.getRestaurantId())))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RuntimeException("Ошибка сервера каталога")))
                .bodyToMono(CatalogRestaurantDTO.class)
                .block();

        if (restaurant == null || !restaurant.isActive()) {
            throw new RuntimeException("Ресторан временно недоступен");
        }

        CatalogMenuItemDTO[] menuArray = webClient.get()
                .uri(catalogServiceUrl + "/api/catalog/restaurants/{restaurantId}/menu-items", request.getRestaurantId())
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("Не удалось получить меню ресторана")))
                .bodyToMono(CatalogMenuItemDTO[].class)
                .block();

        List<CatalogMenuItemDTO> menuItems = List.of(menuArray != null ? menuArray : new CatalogMenuItemDTO[0]);

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

            webClient.post()
                    .uri(courierServiceUrl + "/api/courier/internal/orders/{orderId}/assign", savedOrder.getId())
                    .bodyValue(assignRequest)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

        } catch (WebClientResponseException e) {
            System.out.println("Ошибка при назначении курьера: " + e.getResponseBodyAsString());
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