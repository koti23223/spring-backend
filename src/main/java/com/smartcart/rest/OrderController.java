package com.smartcart.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.smartcart.dto.OrderRequestDto;
import com.smartcart.entity.Order;
import com.smartcart.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody OrderRequestDto request) {
        try {
            System.out.println("Checkout email from frontend: " + request.getUserEmail());

            Order savedOrder = orderService.placeOrder(request);

            System.out.println("Saved order email: " + savedOrder.getUserEmail());

            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                Map.of("message", e.getMessage() == null ? "Failed to save order" : e.getMessage())
            );
        }
    }

    @GetMapping("/track/{email}")
    public ResponseEntity<?> trackOrders(@PathVariable String email) {
        try {
            List<Order> orders = orderService.getOrdersByEmail(email);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                Map.of("message", e.getMessage() == null ? "Failed to fetch orders" : e.getMessage())
            );
        }
    }

    @PutMapping("/status/{orderId}")
    public ResponseEntity<?> updateStatus(@PathVariable String orderId,
                                          @RequestParam String status) {
        try {
            return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                Map.of("message", e.getMessage() == null ? "Failed to update status" : e.getMessage())
            );
        }
    }
}