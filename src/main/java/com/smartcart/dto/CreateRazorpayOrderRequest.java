package com.smartcart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRazorpayOrderRequest {
	private Integer amount;
    private String currency;
    private String receipt;
}
