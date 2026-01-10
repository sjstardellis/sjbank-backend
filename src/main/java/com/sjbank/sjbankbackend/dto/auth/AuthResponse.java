package com.sjbank.sjbankbackend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Generates getters and setters from Lombok
@Data
// Create empty constructor
@NoArgsConstructor
// Create constructor with all fields
@AllArgsConstructor
public class AuthResponse {

    // Response we build for authentication
    private String token;
    private String type = "Bearer";
    private String customerId;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}