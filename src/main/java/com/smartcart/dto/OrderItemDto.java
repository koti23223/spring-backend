package com.smartcart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    private Long productId;
    private String productTitle;
    private String productImageUrl;
    private Double price;
    private Integer quantity;
}