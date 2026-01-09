package com.sjbank.sjbankbackend.controller;

import com.sjbank.sjbankbackend.dto.DepositRequest;
import com.sjbank.sjbankbackend.dto.TransactionDTO;
import com.sjbank.sjbankbackend.dto.TransferRequest;
import com.sjbank.sjbankbackend.dto.WithdrawRequest;
import com.sjbank.sjbankbackend.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // POST - Deposit money into an account
    @PostMapping("/deposit")
    public ResponseEntity<TransactionDTO> deposit(@Valid @RequestBody DepositRequest request) {
        TransactionDTO transaction = transactionService.deposit(
                request.getAccountNumber(),
                request.getAmount(),
                request.getDescription()
        );
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    // POST - Withdraw money from an account
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionDTO> withdraw(@Valid @RequestBody WithdrawRequest request) {
        TransactionDTO transaction = transactionService.withdraw(
                request.getAccountNumber(),
                request.getAmount(),
                request.getDescription()
        );
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    // POST - Transfer money between two accounts
    @PostMapping("/transfer")
    public ResponseEntity<List<TransactionDTO>> transfer(@Valid @RequestBody TransferRequest request) {
        List<TransactionDTO> transactions = transactionService.transfer(
                request.getFromAccountNumber(),
                request.getToAccountNumber(),
                request.getAmount(),
                request.getDescription()
        );
        return new ResponseEntity<>(transactions, HttpStatus.CREATED);
    }

    // GET - Get all transactions for an account
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByAccountNumber(accountNumber);
        return ResponseEntity.ok(transactions);
    }

    // GET - Get transactions for an account within a date range
    @GetMapping("/account/{accountNumber}/date-range")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByDateRange(
            @PathVariable String accountNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByAccountNumberAndDateRange(
                accountNumber, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }
}