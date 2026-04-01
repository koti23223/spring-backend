package com.smartcart.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PRIMARY KEY (important)

    @Column(name = "order_id", unique = true, nullable = false)
    private String orderId;

    private String userEmail;
    private String fullName;
    private String streetAddress;
    private String city;
    private String zipCode;
    private String country;

    private Double subtotal;
    private Double tax;
    private Double totalAmount;

    private String paymentMethod;
    private String paymentId;
    private String razorpayOrderId;

    private String orderStatus;
    private LocalDateTime orderDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_ref_id")
    private List<OrderItem> items;
}