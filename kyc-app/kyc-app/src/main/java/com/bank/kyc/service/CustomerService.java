package com.bank.kyc.service;

import com.bank.kyc.model.Account;
import com.bank.kyc.model.Customer;
import com.bank.kyc.repository.AccountRepository;
import com.bank.kyc.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import com.bank.kyc.service.MailService;
import com.bank.kyc.model.User; // if used explicitly

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repo;

    @Autowired
    private MailService mailService;
    
    @Autowired
    private AccountRepository accountRepo;


    public Customer saveKyc(Customer customer,
            MultipartFile aadhar,
            MultipartFile pan,
            MultipartFile photo) throws IOException {

			String email = customer.getUser().getEmail();
			String uploadDir = "uploads/" + email.replaceAll("[^a-zA-Z0-9]", "_");
			Files.createDirectories(Paths.get(uploadDir));
			
			saveFile(aadhar, uploadDir, "aadhaar");
			saveFile(pan, uploadDir, "pan");
			saveFile(photo, uploadDir, "photo");
			
			customer.setAadharBlob(aadhar.getBytes());
			customer.setPanBlob(pan.getBytes());
			customer.setPhotoBlob(photo.getBytes());
			
			customer.setAadharOriginalName(aadhar.getOriginalFilename());
			customer.setPanOriginalName(pan.getOriginalFilename());
			customer.setPhotoOriginalName(photo.getOriginalFilename());
			
			customer.setAadharContentType(aadhar.getContentType());
			customer.setPanContentType(pan.getContentType());
			customer.setPhotoContentType(photo.getContentType());
			
			customer.setKycStatus("Pending");
			customer.setSubmittedAt(LocalDateTime.now());
			
			Customer saved = repo.save(customer);
			
			mailService.send(email, "KYC Submitted",
			"✅ Your KYC documents were uploaded successfully. Verification is in progress.");
			
			return saved;
		}


    private void saveFile(MultipartFile file, String uploadDir, String label) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String extension = (originalFileName != null && originalFileName.contains("."))
                ? originalFileName.substring(originalFileName.lastIndexOf(".")) : "";
        Path path = Paths.get(uploadDir + "/" + label + extension);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
    }

    public Customer verifyKyc(Long id) {
        return repo.findById(id)
            .map(c -> {
                c.setKycStatus("Verified");
                c.setVerifiedAt(LocalDateTime.now());

                // ✅ Create Savings account if not already assigned
                if (!accountRepo.existsByCustomerIdAndAccountType(id, "Savings")) {
                    Account savings = new Account();
                    savings.setAccountType("Savings");
                    savings.setCustomer(c);
                    accountRepo.save(savings);
                }

                // ✉️ Notify user
                mailService.send(c.getUser().getEmail(), "KYC Verified",
                        "🎉 Your KYC has been verified. Thank you!");

                return repo.save(c);
            })
            .orElseThrow(() -> new RuntimeException("❌ Customer not found"));
    }


    public Customer rejectKyc(Long id, String reason) {
        return repo.findById(id)
                .map(c -> {
                    c.setKycStatus("Rejected");
                    c.setVerifiedAt(null);
                    c.setRejectionReason(reason);

                    repo.save(c);

                    mailService.send(c.getUser().getEmail(), "KYC Rejected",
                            "⚠️ Your KYC submission was rejected.\nReason: " + reason);

                    return c;
                })
                .orElseThrow(() -> new RuntimeException("❌ Customer not found"));
    }

    public List<Customer> getAll() {
        return repo.findAll();
    }

    public Customer getByEmail(String email) {
        return repo.findByUserEmail(email.trim())
                .orElseThrow(() -> new RuntimeException("No KYC found"));
    }

    public Customer getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));
    }
    
    public Customer resubmitKyc(Long customerId,
            MultipartFile aadhar,
            MultipartFile pan,
            MultipartFile photo) throws IOException {
			Customer customer = getById(customerId);
			
			if (!"Rejected".equalsIgnoreCase(customer.getKycStatus())) {
			throw new RuntimeException("⚠️ Only rejected customers can re-upload documents.");
			}
			
			// Replace BLOBs
			customer.setAadharBlob(aadhar.getBytes());
			customer.setPanBlob(pan.getBytes());
			customer.setPhotoBlob(photo.getBytes());
			
			// Replace metadata
			customer.setAadharOriginalName(aadhar.getOriginalFilename());
			customer.setPanOriginalName(pan.getOriginalFilename());
			customer.setPhotoOriginalName(photo.getOriginalFilename());
			
			customer.setAadharContentType(aadhar.getContentType());
			customer.setPanContentType(pan.getContentType());
			customer.setPhotoContentType(photo.getContentType());
			
			// Reset status
			customer.setKycStatus("Pending");
			customer.setSubmittedAt(LocalDateTime.now());
			customer.setVerifiedAt(null);
			customer.setRejectionReason(null); // Clear old reason
			
			repo.save(customer);
			
			mailService.send(customer.getUser().getEmail(), "KYC Re-uploaded",
			"✅ Your new documents have been submitted successfully. Verification will begin shortly.");
			
			return customer;
    }

}

