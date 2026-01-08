package com.sjbank.sjbankbackend.repository;

import com.sjbank.sjbankbackend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    // Find a customer by their email address
    Optional<Customer> findByEmail(String email);

    // Check if a customer exists with given email
    boolean existsByEmail(String email);
}