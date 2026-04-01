package com.smartcart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CheckoutResponse {

    private Long orderId;
    private String message;
    private Double subtotal;
    private Double gstAmount;
    private Double grandTotal;

}