package com.smartcart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartcart.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserEmailOrderByOrderDateDesc(String userEmail);

    Optional<Order> findByOrderId(String orderId);
}