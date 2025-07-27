package com.bank.kyc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "account")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
	@SequenceGenerator(name = "account_seq", sequenceName = "account_seq", allocationSize = 1)
	private Long id;


    private String accountType; // "Savings" or "Current"

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
}
