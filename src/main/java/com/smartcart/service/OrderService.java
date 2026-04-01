package com.smartcart.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.smartcart.dto.OrderItemDto;
import com.smartcart.dto.OrderRequestDto;
import com.smartcart.entity.Order;
import com.smartcart.entity.OrderItem;
import com.smartcart.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final EmailService emailService;

    public OrderService(OrderRepository orderRepository, EmailService emailService) {
        this.orderRepository = orderRepository;
        this.emailService = emailService;
    }

    public Order placeOrder(OrderRequestDto request) {
        validateOrderRequest(request);

        Order order = new Order();
        order.setOrderId(generateOrderId());
        order.setUserEmail(request.getUserEmail());
        order.setFullName(request.getFullName());
        order.setStreetAddress(request.getStreetAddress());
        order.setCity(request.getCity());
        order.setZipCode(request.getZipCode());
        order.setCountry(request.getCountry());

        order.setSubtotal(request.getSubtotal());
        order.setTax(request.getTax());
        order.setTotalAmount(request.getTotalAmount());

        order.setPaymentMethod(request.getPaymentMethod());
        order.setPaymentId(request.getPaymentId());
        order.setRazorpayOrderId(request.getRazorpayOrderId());

        order.setOrderStatus("PLACED");
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> items = request.getCartItems()
                .stream()
                .map(this::mapToOrderItem)
                .collect(Collectors.toList());

        order.setItems(items);

//        Order savedOrder = orderRepository.save(order);
//
//        try {
//            System.out.println("Sending email to: " + savedOrder.getUserEmail());
//            emailService.sendOrderConfirmationEmail(savedOrder);
//            System.out.println("Order confirmation email sent successfully");
//        } catch (Exception e) {
//            System.out.println("Order saved, but email sending failed: " + e.getMessage());
//            e.printStackTrace();
//        }
        Order savedOrder = orderRepository.save(order);

        try {
            emailService.sendOrderConfirmationEmail(savedOrder);
        } catch (Exception e) {
            System.out.println("Order saved, but email sending failed: " + e.getMessage());
            e.printStackTrace();
        }

        return savedOrder;
    }

    public List<Order> getOrdersByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new RuntimeException("Email is required");
        }
        return orderRepository.findByUserEmailOrderByOrderDateDesc(email);
    }

    public Order updateOrderStatus(String orderId, String status) {
        if (orderId == null || orderId.isBlank()) {
            throw new RuntimeException("Order ID is required");
        }

        if (status == null || status.isBlank()) {
            throw new RuntimeException("Status is required");
        }

        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(status.toUpperCase());
        return orderRepository.save(order);
    }

    private void validateOrderRequest(OrderRequestDto request) {
        if (request == null) {
            throw new RuntimeException("Order request is missing");
        }
        if (request.getUserEmail() == null || request.getUserEmail().isBlank()) {
            throw new RuntimeException("User email is missing");
        }
        if (request.getFullName() == null || request.getFullName().isBlank()) {
            throw new RuntimeException("Full name is required");
        }
        if (request.getStreetAddress() == null || request.getStreetAddress().isBlank()) {
            throw new RuntimeException("Street address is required");
        }
        if (request.getCity() == null || request.getCity().isBlank()) {
            throw new RuntimeException("City is required");
        }
        if (request.getZipCode() == null || request.getZipCode().isBlank()) {
            throw new RuntimeException("Zip code is required");
        }
        if (request.getCountry() == null || request.getCountry().isBlank()) {
            throw new RuntimeException("Country is required");
        }
        if (request.getPaymentMethod() == null || request.getPaymentMethod().isBlank()) {
            throw new RuntimeException("Payment method is required");
        }
        if (request.getPaymentId() == null || request.getPaymentId().isBlank()) {
            throw new RuntimeException("Payment ID is required");
        }
        if (request.getRazorpayOrderId() == null || request.getRazorpayOrderId().isBlank()) {
            throw new RuntimeException("Razorpay order ID is required");
        }
        if (request.getCartItems() == null || request.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart items are required");
        }
    }

    private OrderItem mapToOrderItem(OrderItemDto dto) {
        if (dto.getProductId() == null) {
            throw new RuntimeException("Product ID is missing");
        }
        if (dto.getProductTitle() == null || dto.getProductTitle().isBlank()) {
            throw new RuntimeException("Product title is missing");
        }
        if (dto.getPrice() == null) {
            throw new RuntimeException("Product price is missing");
        }
        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            throw new RuntimeException("Invalid product quantity");
        }

        OrderItem item = new OrderItem();
        item.setProductId(dto.getProductId());
        item.setProductTitle(dto.getProductTitle());
        item.setProductImageUrl(dto.getProductImageUrl());
        item.setPrice(dto.getPrice());
        item.setQuantity(dto.getQuantity());
        return item;
    }

    private String generateOrderId() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}