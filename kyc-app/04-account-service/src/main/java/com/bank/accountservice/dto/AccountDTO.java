package com.bank.accountservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AccountDTO {

    private Long id;

    @NotBlank(message = "Account type is required")
    private String accountType;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    public AccountDTO() {}

    public AccountDTO(Long id, String accountType, Long customerId) {
        this.id = id;
        this.accountType = accountType;
        this.customerId = customerId;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
}
