package com.sjbank.sjbankbackend.service;

import com.sjbank.sjbankbackend.dto.TransactionDTO;
import com.sjbank.sjbankbackend.entity.Account;
import com.sjbank.sjbankbackend.entity.Transaction;
import com.sjbank.sjbankbackend.repository.AccountRepository;
import com.sjbank.sjbankbackend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    // Deposit money into Account
    public TransactionDTO deposit(String accountNumber, BigDecimal amount, String description) {
        // Validate amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Deposit amount must be greater than zero");
        }

        // Find account
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));

        // Check account status
        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new RuntimeException("Account is not active. Current status: " + account.getStatus());
        }

        // Update balance
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setTransactionReference(generateTransactionReference());
        transaction.setTransactionType(Transaction.TransactionType.DEPOSIT);
        transaction.setAmount(amount);
        transaction.setBalanceAfter(account.getBalance());
        transaction.setAccount(account);
        transaction.setDescription(description != null ? description : "Deposit");

        // Save and return
        Transaction savedTransaction = transactionRepository.save(transaction);
        return new TransactionDTO(savedTransaction);
    }

    // Withdraw money from Account
    public TransactionDTO withdraw(String accountNumber, BigDecimal amount, String description) {
        // Validate amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Withdrawal amount must be greater than zero");
        }

        // Find account
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));

        // Check account status
        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new RuntimeException("Account is not active. Current status: " + account.getStatus());
        }

        // Check sufficient funds
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds. Current balance: $" + account.getBalance()
                    + ", Requested withdrawal: $" + amount);
        }

        // Update balance
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setTransactionReference(generateTransactionReference());
        transaction.setTransactionType(Transaction.TransactionType.WITHDRAWAL);
        transaction.setAmount(amount);
        transaction.setBalanceAfter(account.getBalance());
        transaction.setAccount(account);
        transaction.setDescription(description != null ? description : "Withdrawal");

        // Save and return
        Transaction savedTransaction = transactionRepository.save(transaction);
        return new TransactionDTO(savedTransaction);
    }

    // Transfer money between two Accounts
    public List<TransactionDTO> transfer(String fromAccountNumber, String toAccountNumber,
                                         BigDecimal amount, String description) {
        // Validate amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Transfer amount must be greater than zero");
        }

        // Check not transferring to same Account
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new RuntimeException("Cannot transfer to the same account");
        }

        // Find both accounts
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new RuntimeException("Source account not found: " + fromAccountNumber));

        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("Target account not found: " + toAccountNumber));

        // Check both accounts are active
        if (fromAccount.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new RuntimeException("Source account is not active. Status: " + fromAccount.getStatus());
        }

        if (toAccount.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new RuntimeException("Target account is not active. Status: " + toAccount.getStatus());
        }

        // Check sufficient funds
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds in source account. Balance: $"
                    + fromAccount.getBalance() + ", Requested: $" + amount);
        }

        // Update balances
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Generate transaction reference
        String transferRef = generateTransactionReference();

        // Create TRANSFER_OUT transaction record to the source account
        Transaction debitTransaction = new Transaction();
        debitTransaction.setTransactionReference(transferRef + "-OUT");
        debitTransaction.setTransactionType(Transaction.TransactionType.TRANSFER_OUT);
        debitTransaction.setAmount(amount);
        debitTransaction.setBalanceAfter(fromAccount.getBalance());
        debitTransaction.setAccount(fromAccount);
        debitTransaction.setTargetAccountNumber(toAccountNumber);
        debitTransaction.setDescription(description != null ? description : "Transfer to " + toAccountNumber);
        Transaction savedDebitTx = transactionRepository.save(debitTransaction);

        // Create TRANSFER_IN transaction record to the target account
        Transaction creditTransaction = new Transaction();
        creditTransaction.setTransactionReference(transferRef + "-IN");
        creditTransaction.setTransactionType(Transaction.TransactionType.TRANSFER_IN);
        creditTransaction.setAmount(amount);
        creditTransaction.setBalanceAfter(toAccount.getBalance());
        creditTransaction.setAccount(toAccount);
        creditTransaction.setTargetAccountNumber(fromAccountNumber);
        creditTransaction.setDescription(description != null ? description : "Transfer from " + fromAccountNumber);
        Transaction savedCreditTx = transactionRepository.save(creditTransaction);

        // Return both transactions
        return List.of(new TransactionDTO(savedDebitTx), new TransactionDTO(savedCreditTx));
    }

    // Get all transactions for a specific Account
    public List<TransactionDTO> getTransactionsByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));

        return transactionRepository.findByAccountIdOrderByCreatedAtDesc(account.getId()).stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
    }

    // Get all transactions for a specific date range
    public List<TransactionDTO> getTransactionsByAccountNumberAndDateRange(
            String accountNumber, LocalDateTime startDate, LocalDateTime endDate) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));

        return transactionRepository
                .findByAccountIdAndCreatedAtBetweenOrderByCreatedAtDesc(account.getId(), startDate, endDate)
                .stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
    }

    // Generate a unique transaction reference UUID
    private String generateTransactionReference() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}