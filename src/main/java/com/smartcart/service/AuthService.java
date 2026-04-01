package com.smartcart.service;

import com.smartcart.dto.LoginRequest;
import com.smartcart.dto.RegisterRequest;
import com.smartcart.dto.ResetPasswordRequest;

public interface AuthService {
	
	String register(RegisterRequest request);

    String login(LoginRequest request);
    
    String resetPassword(ResetPasswordRequest request);
    String sendOtp(String email);
}
