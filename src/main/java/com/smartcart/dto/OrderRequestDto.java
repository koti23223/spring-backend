package com.smartcart.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDto {

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

    private List<OrderItemDto> cartItems;
}