package com.bank.kyc.model;

public class KycResponse {
    private String status;
    private String message;
    private Long customerId;

    public KycResponse(String status, String message, Long customerId) {
        this.status = status;
        this.message = message;
        this.customerId = customerId;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

    
}
