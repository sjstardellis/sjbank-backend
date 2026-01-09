package com.sjbank.sjbankbackend.controller;

import com.sjbank.sjbankbackend.dto.AccountDTO;
import com.sjbank.sjbankbackend.entity.Account;
import com.sjbank.sjbankbackend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // POST - Create a new account for a customer
    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(
            @RequestParam String customerId,
            @RequestParam Account.AccountType accountType) {
        AccountDTO createdAccount = accountService.createAccount(customerId, accountType);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }


    // GET - Get account by Id
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable String id) {
        AccountDTO account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }


    // GET - Get account by account number
    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<AccountDTO> getAccountByAccountNumber(@PathVariable String accountNumber) {
        AccountDTO account = accountService.getAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(account);
    }


    // GET - Get all accounts for a specific Customer Id
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountDTO>> getAccountsByCustomerId(@PathVariable String customerId) {
        List<AccountDTO> accounts = accountService.getAccountsByCustomerId(customerId);
        return ResponseEntity.ok(accounts);
    }


    // GET - Get all Accounts
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<AccountDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    // PATCH - Update account status
    @PatchMapping("/{id}/status")
    public ResponseEntity<AccountDTO> updateAccountStatus(
            @PathVariable String id,
            @RequestParam Account.AccountStatus status) {
        AccountDTO updatedAccount = accountService.updateAccountStatus(id, status);
        return ResponseEntity.ok(updatedAccount);
    }

    // DELETE - Delete account (only if balance is zero)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}