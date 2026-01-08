package com.sjbank.sjbankbackend.dto;

import com.sjbank.sjbankbackend.entity.Transaction;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Generates getters and setters from Lombok
@Data
// Create empty constructor
@NoArgsConstructor
// Create constructor with all fields
@AllArgsConstructor
public class TransactionDTO {

    private String id;
    private String transactionReference;

    @NotNull(message = "Transaction type is required")
    private Transaction.TransactionType transactionType;

    // Ensuring value is greater than $0.00
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    private BigDecimal balanceAfter;
    private String accountNumber;
    private String description;
    private String targetAccountNumber;
    private LocalDateTime createdAt;

    // Constructor from entity
    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.transactionReference = transaction.getTransactionReference();
        this.transactionType = transaction.getTransactionType();
        this.amount = transaction.getAmount();
        this.balanceAfter = transaction.getBalanceAfter();
        this.accountNumber = transaction.getAccount().getAccountNumber();
        this.description = transaction.getDescription();
        this.targetAccountNumber = transaction.getTargetAccountNumber();
        this.createdAt = transaction.getCreatedAt();
    }
}