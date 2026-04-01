package com.smartcart.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutRequest {

    private String userEmail;
    private String fullName;
    private String streetAddress;
    private String city;
    private String zipCode;
    private String country;

    private String paymentMethod;
    private String paymentId;
    private String razorpayOrderId;

    private List<CartItemRequest> cartItems;

}