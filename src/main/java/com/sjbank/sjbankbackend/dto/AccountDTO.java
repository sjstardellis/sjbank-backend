package com.sjbank.sjbankbackend.dto;

import com.sjbank.sjbankbackend.entity.Account;
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
public class AccountDTO {

    private String id;
    private String accountNumber;

    @NotNull(message = "Account type is required")
    private Account.AccountType accountType;

    private BigDecimal balance;
    private Account.AccountStatus status;
    private String customerId;
    private String customerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor from entity
    public AccountDTO(Account account) {
        this.id = account.getId();
        this.accountNumber = account.getAccountNumber();
        this.accountType = account.getAccountType();
        this.balance = account.getBalance();
        this.status = account.getStatus();
        this.customerId = account.getCustomer().getId();
        this.customerName = account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName();
        this.createdAt = account.getCreatedAt();
        this.updatedAt = account.getUpdatedAt();
    }
}