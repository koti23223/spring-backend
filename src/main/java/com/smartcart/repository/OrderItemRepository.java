package com.smartcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartcart.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
