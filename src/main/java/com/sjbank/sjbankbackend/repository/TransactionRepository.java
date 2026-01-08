package com.sjbank.sjbankbackend.repository;

import com.sjbank.sjbankbackend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    // Find all transactions for a specific account, ordered by newest first
    List<Transaction> findByAccountIdOrderByCreatedAtDesc(String accountId);

    // Find transactions for an account within a date range, ordered by newest first
    List<Transaction> findByAccountIdAndCreatedAtBetweenOrderByCreatedAtDesc(String accountId, LocalDateTime startDate, LocalDateTime endDate);
}