package com.smartcart.dto;

import lombok.Data;

@Data
public class ProfileResponse {
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String bio;
    private String profileImage;
    private String message;
}