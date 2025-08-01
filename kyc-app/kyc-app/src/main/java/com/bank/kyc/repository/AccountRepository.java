package com.bank.kyc.repository;

import com.bank.kyc.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomerId(Long customerId);
    boolean existsByCustomerIdAndAccountType(Long customerId, String accountType);
}
