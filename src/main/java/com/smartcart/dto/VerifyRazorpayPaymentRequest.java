package com.smartcart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyRazorpayPaymentRequest {
	private String razorpayOrderId;
	private String razorpayPaymentId;
	private String razorpaySignature;
}
