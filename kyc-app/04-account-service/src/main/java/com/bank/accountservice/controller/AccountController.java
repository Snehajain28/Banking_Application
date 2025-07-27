package com.bank.accountservice.controller;

import com.bank.accountservice.dto.AccountDTO;
import com.bank.accountservice.entity.Account;
import com.bank.accountservice.exception.ResourceNotFoundException;
import com.bank.accountservice.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@Valid @RequestBody AccountDTO dto) {
        return ResponseEntity.ok(accountService.createAccount(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id)));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountDTO>> getAccountsByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(accountService.getAccountsByCustomerId(customerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long id, @Valid @RequestBody AccountDTO dto) {
        return ResponseEntity.ok(accountService.updateAccount(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
