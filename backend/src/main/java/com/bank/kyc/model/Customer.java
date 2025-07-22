package com.bank.kyc.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
    @SequenceGenerator(name = "customer_seq", sequenceName = "customer_seq", allocationSize = 1)
    private Long id;

    private String name;
    private String kycStatus = "Pending";
    
    @CreationTimestamp
    private LocalDateTime submittedAt;

    @UpdateTimestamp
    private LocalDateTime verifiedAt;
    
    private String aadharOriginalName;
    private String panOriginalName;
    private String photoOriginalName;

    private String aadharContentType;
    private String panContentType;
    private String photoContentType;

    
    public String getAadharOriginalName() {
		return aadharOriginalName;
	}

	public void setAadharOriginalName(String aadharOriginalName) {
		this.aadharOriginalName = aadharOriginalName;
	}

	public String getPanOriginalName() {
		return panOriginalName;
	}

	public void setPanOriginalName(String panOriginalName) {
		this.panOriginalName = panOriginalName;
	}

	public String getPhotoOriginalName() {
		return photoOriginalName;
	}

	public void setPhotoOriginalName(String photoOriginalName) {
		this.photoOriginalName = photoOriginalName;
	}

	public String getAadharContentType() {
		return aadharContentType;
	}

	public void setAadharContentType(String aadharContentType) {
		this.aadharContentType = aadharContentType;
	}

	public String getPanContentType() {
		return panContentType;
	}

	public void setPanContentType(String panContentType) {
		this.panContentType = panContentType;
	}

	public String getPhotoContentType() {
		return photoContentType;
	}

	public void setPhotoContentType(String photoContentType) {
		this.photoContentType = photoContentType;
	}

	@Lob
    private byte[] aadharBlob;

    @Lob
    private byte[] panBlob;

    @Lob
    private byte[] photoBlob;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKycStatus() {
		return kycStatus;
	}

	public void setKycStatus(String kycStatus) {
		this.kycStatus = kycStatus;
	}

	public byte[] getAadharBlob() {
		return aadharBlob;
	}

	public void setAadharBlob(byte[] aadharBlob) {
		this.aadharBlob = aadharBlob;
	}

	public byte[] getPanBlob() {
		return panBlob;
	}

	public void setPanBlob(byte[] panBlob) {
		this.panBlob = panBlob;
	}

	public byte[] getPhotoBlob() {
		return photoBlob;
	}

	public void setPhotoBlob(byte[] photoBlob) {
		this.photoBlob = photoBlob;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

 
}
