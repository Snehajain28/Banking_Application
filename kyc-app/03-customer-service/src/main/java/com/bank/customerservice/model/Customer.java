package com.bank.customerservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    private String kycStatus;
    
    private String email;  // ✅ ADD THIS

    @Lob
    private byte[] aadharBlob;
    private String aadharContentType;
    private String aadharOriginalName;

    @Lob
    private byte[] panBlob;
    private String panContentType;
    private String panOriginalName;

    @Lob
    private byte[] photoBlob;
    private String photoContentType;
    private String photoOriginalName;

    // ✅ Getters and Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getKycStatus() { return kycStatus; }

    public void setKycStatus(String kycStatus) { this.kycStatus = kycStatus; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public byte[] getAadharBlob() { return aadharBlob; }

    public void setAadharBlob(byte[] aadharBlob) { this.aadharBlob = aadharBlob; }

    public String getAadharContentType() { return aadharContentType; }

    public void setAadharContentType(String aadharContentType) { this.aadharContentType = aadharContentType; }

    public String getAadharOriginalName() { return aadharOriginalName; }

    public void setAadharOriginalName(String aadharOriginalName) { this.aadharOriginalName = aadharOriginalName; }

    public byte[] getPanBlob() { return panBlob; }

    public void setPanBlob(byte[] panBlob) { this.panBlob = panBlob; }

    public String getPanContentType() { return panContentType; }

    public void setPanContentType(String panContentType) { this.panContentType = panContentType; }

    public String getPanOriginalName() { return panOriginalName; }

    public void setPanOriginalName(String panOriginalName) { this.panOriginalName = panOriginalName; }

    public byte[] getPhotoBlob() { return photoBlob; }

    public void setPhotoBlob(byte[] photoBlob) { this.photoBlob = photoBlob; }

    public String getPhotoContentType() { return photoContentType; }

    public void setPhotoContentType(String photoContentType) { this.photoContentType = photoContentType; }

    public String getPhotoOriginalName() { return photoOriginalName; }

    public void setPhotoOriginalName(String photoOriginalName) { this.photoOriginalName = photoOriginalName; }
}
