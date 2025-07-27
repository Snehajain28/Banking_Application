package com.bank.kyc.model;

import java.time.LocalDateTime;
import java.util.List;

public class CustomerStatus {
    private String kycStatus;
    private LocalDateTime submittedAt;
    private LocalDateTime verifiedAt;

    public CustomerStatus(Customer c) {
        this.kycStatus = c.getKycStatus();
        this.submittedAt = c.getSubmittedAt();
        this.verifiedAt = c.getVerifiedAt();
    }

	public String getKycStatus() {
		return kycStatus;
	}

	public void setKycStatus(String kycStatus) {
		this.kycStatus = kycStatus;
	}

	public LocalDateTime getSubmittedAt() {
		return submittedAt;
	}

	public void setSubmittedAt(LocalDateTime submittedAt) {
		this.submittedAt = submittedAt;
	}

	public LocalDateTime getVerifiedAt() {
		return verifiedAt;
	}

	public void setVerifiedAt(LocalDateTime verifiedAt) {
		this.verifiedAt = verifiedAt;
	}
	
	private List<String> accounts;

	public List<String> getAccounts() { 
		return accounts; 
	}
	
	public void setAccounts(List<String> accounts) { 
		this.accounts = accounts; 
	}

}

