package com.sjbank.sjbankbackend.service;

import com.sjbank.sjbankbackend.dto.AccountDTO;
import com.sjbank.sjbankbackend.entity.Account;
import com.sjbank.sjbankbackend.entity.Customer;
import com.sjbank.sjbankbackend.repository.AccountRepository;
import com.sjbank.sjbankbackend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // Create a new Account to a Customer
    public AccountDTO createAccount(String customerId, Account.AccountType accountType) {
        // Find customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        // Create new account
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setAccountType(accountType);
        account.setBalance(BigDecimal.ZERO);
        account.setStatus(Account.AccountStatus.ACTIVE);
        account.setCustomer(customer);

        // Save and return
        Account savedAccount = accountRepository.save(account);
        return new AccountDTO(savedAccount);
    }

    // Get Account by Id
    public AccountDTO getAccountById(String id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        return new AccountDTO(account);
    }

    // Get Account by number
    public AccountDTO getAccountByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found with number: " + accountNumber));
        return new AccountDTO(account);
    }

    // Get all Accounts for a Customer by Id
    public List<AccountDTO> getAccountsByCustomerId(String customerId) {
        return accountRepository.findByCustomerId(customerId).stream()
                .map(AccountDTO::new)
                .collect(Collectors.toList());
    }

    // Get all Accounts
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(AccountDTO::new)
                .collect(Collectors.toList());
    }

    // Update Account status
    public AccountDTO updateAccountStatus(String id, Account.AccountStatus status) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));

        account.setStatus(status);
        Account updatedAccount = accountRepository.save(account);
        return new AccountDTO(updatedAccount);
    }

    // Delete Account, must have balance of $0.00
    public void deleteAccount(String id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));

        // Check balance before deletion
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new RuntimeException("Cannot delete account with non-zero balance. Current balance: $"
                    + account.getBalance());
        }

        accountRepository.deleteById(id);
    }

    // Generate unique account number using UUID
    private String generateAccountNumber() {
        String accountNumber;
        do {
            // Generate UUID-based account number
            accountNumber = "ACC-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }
}