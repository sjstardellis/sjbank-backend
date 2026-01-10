package com.sjbank.sjbankbackend.dto;

import com.sjbank.sjbankbackend.entity.Customer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Generates getters and setters from Lombok
@Data
// Create empty constructor
@NoArgsConstructor
// Create constructor with all fields
@AllArgsConstructor
public class CustomerDTO {

    private String id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    // Don't include password in regular DTO

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor from entity
    public CustomerDTO(Customer customer) {
        this.id = customer.getId();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.email = customer.getEmail();
        this.phoneNumber = customer.getPhoneNumber();
        this.address = customer.getAddress();
        this.city = customer.getCity();
        this.state = customer.getState();
        this.zipCode = customer.getZipCode();
        this.role = customer.getRole().name();
        this.createdAt = customer.getCreatedAt();
        this.updatedAt = customer.getUpdatedAt();
    }
}