package com.sjbank.sjbankbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
// Generates getters and setters from Lombok
@Data
// Create empty constructor
@NoArgsConstructor
// Create constructor with all fields
@AllArgsConstructor
public class Customer {

    // Primary key for each customer
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // Require first name
    @NotBlank(message = "First name is required")
    @Column(nullable = false)
    private String firstName;

    // Require last name
    @NotBlank(message = "Last name is required")
    @Column(nullable = false)
    private String lastName;

    // Require email
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;

    // Require phone number
    @NotBlank(message = "Phone number is required")
    // Regex for valid phone number
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number should be valid")
    @Column(nullable = false)
    private String phoneNumber;

    // Optional fields
    @Column()
    private String address;

    @Column()
    private String city;

    @Column()
    private String state;

    @Column()
    private String zipCode;

    // One customer can have many accounts
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();

    // Creation timestamp
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Updates whenever a row changes
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Add account
    public void addAccount(Account account) {
        accounts.add(account);
        account.setCustomer(this);
    }

    // Remove account
    public void removeAccount(Account account) {
        accounts.remove(account);
        account.setCustomer(null);
    }

}