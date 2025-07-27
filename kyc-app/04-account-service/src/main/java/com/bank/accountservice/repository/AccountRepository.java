package com.bank.accountservice.repository;

import com.bank.accountservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomerId(Long customerId);
}
