package com.sjbank.sjbankbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
// Generates getters and setters from Lombok
@Data
// Create empty constructor
@NoArgsConstructor
// Create constructor with all fields
@AllArgsConstructor
public class Account {

    // Primary key for each account
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // Public number used for account interactions
    @Column(nullable = false, unique = true, length = 50)
    private String accountNumber;

    // Account type, whether it is CHECKING, SAVINGS, BUSINESS, CREDIT
    @NotNull(message = "Account type is required")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    // Balance, must be > $0.00
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance cannot be negative")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    // Account status
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;

    // Each account can only go to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // One account can have many transactions
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    // Creation timestamp
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Updates whenever a row changes
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Add transaction
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setAccount(this);
    }

    // Different selections for each account type
    public enum AccountType {
        CHECKING, SAVINGS, BUSINESS, CREDIT
    }

    public enum AccountStatus {
        ACTIVE, INACTIVE, CLOSED, FROZEN
    }
}