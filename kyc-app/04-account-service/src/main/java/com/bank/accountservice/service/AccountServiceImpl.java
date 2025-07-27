package com.bank.accountservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.accountservice.dto.AccountDTO;
import com.bank.accountservice.entity.Account;
import com.bank.accountservice.exception.ResourceNotFoundException;
import com.bank.accountservice.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    private AccountDTO toDto(Account account) {
        return new AccountDTO(account.getId(), account.getAccountType(), account.getCustomerId());
    }

    private Account toEntity(AccountDTO dto) {
        Account account = new Account();
        account.setId(dto.getId());
        account.setAccountType(dto.getAccountType());
        account.setCustomerId(dto.getCustomerId());
        return account;
    }

    @Override
    public AccountDTO createAccount(AccountDTO dto) {
        Account account = toEntity(dto);
        account = accountRepository.save(account);
        return toDto(account);
    }

    @Override
    public Optional<AccountDTO> getAccountById(Long id) {
        return accountRepository.findById(id).map(this::toDto);
    }

    @Override
    public List<AccountDTO> getAccountsByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDTO updateAccount(Long id, AccountDTO dto) {
        Account existing = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        existing.setAccountType(dto.getAccountType());
        existing.setCustomerId(dto.getCustomerId());
        return toDto(accountRepository.save(existing));
    }

    @Override
    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Account not found with id: " + id);
        }
        accountRepository.deleteById(id);
    }
}
