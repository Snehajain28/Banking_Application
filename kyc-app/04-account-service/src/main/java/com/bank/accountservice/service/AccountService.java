package com.bank.accountservice.service;

import com.bank.accountservice.dto.AccountDTO;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    AccountDTO createAccount(AccountDTO dto);
    Optional<AccountDTO> getAccountById(Long id);
    List<AccountDTO> getAccountsByCustomerId(Long customerId);
    AccountDTO updateAccount(Long id, AccountDTO dto);
    void deleteAccount(Long id);
}
