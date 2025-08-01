package com.bank.kyc.repository;

import com.bank.kyc.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByKycStatus(String status);
    Optional<Customer> findByUserEmail(String email);

}
