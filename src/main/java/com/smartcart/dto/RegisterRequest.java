package com.smartcart.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
	@NotBlank
    private String fullName;

    @Email
    @NotBlank
    private String email;

    @Size(min = 6,message="Password Must be atleast 6 Characters")
    private String password;

    @Size(min = 6,message="Password Must be atleast 6 Characters")
    private String confirmPassword;

}
