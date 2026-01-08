package com.sjbank.sjbankbackend.repository;

import com.sjbank.sjbankbackend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    // Find an account by its account number
    Optional<Account> findByAccountNumber(String accountNumber);

    // Find all accounts belonging to a specific customer
    List<Account> findByCustomerId(String customerId);

    // Check if an account exists with the given account number
    boolean existsByAccountNumber(String accountNumber);
}