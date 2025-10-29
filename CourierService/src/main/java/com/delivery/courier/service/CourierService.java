package com.delivery.courier.service;

import com.delivery.courier.model.*;
import com.delivery.courier.repository.CourierRepository;
import com.delivery.courier.repository.DeliveryAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourierService {

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private DeliveryAssignmentRepository deliveryAssignmentRepository;

    public DeliveryAssignment assignOrder(String orderId, AssignOrderRequest request) {
        // Находим свободного курьера
        Optional<Courier> availableCourier = courierRepository.findByStatus("AVAILABLE").stream().findFirst();

        if (availableCourier.isEmpty()) {
            throw new RuntimeException("Нет доступных курьеров");
        }

        Courier courier = availableCourier.get();

        DeliveryAssignment assignment = new DeliveryAssignment();
        assignment.setId(UUID.randomUUID().toString());
        assignment.setOrderId(orderId);
        assignment.setCourierId(courier.getId());
        assignment.setRestaurantAddress(request.getRestaurantAddress());
        assignment.setDeliveryAddress(request.getDeliveryAddress());
        assignment.setStatus("ASSIGNED");

        courier.setStatus("BUSY");
        courierRepository.save(courier);

        return deliveryAssignmentRepository.save(assignment);
    }

    public List<DeliveryAssignment> getCourierAssignments(String courierId) {
        return deliveryAssignmentRepository.findByCourier_Id(courierId);
    }

    public Optional<DeliveryAssignment> updateDeliveryStatus(String assignmentId, String courierId, String status) {
        return deliveryAssignmentRepository.findByIdAndCourier_Id(assignmentId, courierId)
                .map(assignment -> {
                    assignment.setStatus(status);
                    return deliveryAssignmentRepository.save(assignment);
                });
    }

    public Optional<DeliveryStatus> trackOrder(String orderId) {
        return deliveryAssignmentRepository.findByOrderId(orderId)
                .map(assignment -> {
                    // Находим курьера для имени
                    String courierName = courierRepository.findById(assignment.getCourierId())
                            .map(Courier::getName)
                            .orElse("Неизвестный курьер");

                    DeliveryStatus status = new DeliveryStatus();
                    status.setOrderId(assignment.getOrderId());
                    status.setStatus(assignment.getStatus());
                    status.setCourierName(courierName);
                    status.setEstimatedTime("30 минут");

                    return status;
                });
    }

    public Courier addNewCourier(CreateCourierRequest request) {
        Courier newCourier = new Courier(request.getName(), request.getPhoneNumber());
        return courierRepository.save(newCourier);
    }
}