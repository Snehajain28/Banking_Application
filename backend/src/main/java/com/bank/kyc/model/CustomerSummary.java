package com.bank.kyc.model;

import java.time.LocalDateTime;

public class CustomerSummary {
    private Long id;
    private String name;
    private String kycStatus;

    private String email;
    private LocalDateTime submittedAt;
    private LocalDateTime verifiedAt;

    public CustomerSummary(Customer c) {
        this.id = c.getId();
        this.name = c.getName();
        this.kycStatus = c.getKycStatus();
        this.email = c.getUser() != null ? c.getUser().getEmail() : "Unlinked";
        this.submittedAt = c.getSubmittedAt();
        this.verifiedAt = c.getVerifiedAt();
    }


    // Getters and setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getKycStatus() { return kycStatus; }
    public String getEmail() { return email; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public LocalDateTime getVerifiedAt() { return verifiedAt; }

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setKycStatus(String kycStatus) {
		this.kycStatus = kycStatus;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setSubmittedAt(LocalDateTime submittedAt) {
		this.submittedAt = submittedAt;
	}

	public void setVerifiedAt(LocalDateTime verifiedAt) {
		this.verifiedAt = verifiedAt;
	}

}

