package com.sjbank.sjbankbackend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Generates getters and setters from Lombok
@Data
// Create empty constructor
@NoArgsConstructor
// Create constructor with all fields
@AllArgsConstructor
public class LoginRequest {

    // Email and password are required for login
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}