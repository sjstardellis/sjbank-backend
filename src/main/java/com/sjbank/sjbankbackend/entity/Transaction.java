package com.sjbank.sjbankbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
// Generates getters and setters from Lombok
@Data
// Create empty constructor
@NoArgsConstructor
// Create constructor with all fields
@AllArgsConstructor
public class Transaction {

    // Primary key for each transaction
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // Transaction reference
    @Column(nullable = false, unique = true, length = 50)
    private String transactionReference;

    // Must choose between different transaction types
    @NotNull(message = "Transaction type is required")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    // Amount greater than $0.00 to transfer
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    // Balance after transaction
    @Column(precision = 19, scale = 2)
    private BigDecimal balanceAfter;

    // Many transactions go to one account
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // Description of transaction, no longer than 500 characters
    @Column(length = 500)
    private String description;

    // For transfers, reference to the target account number
    @Column(length = 20)
    private String targetAccountNumber;

    // Transaction time stamp
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Deposit/withdraw for own account
    // Transfer in/out depending on if sending or receiving money
    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT
    }

}