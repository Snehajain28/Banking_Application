package com.bank.kyc.service;

import com.bank.kyc.model.Customer;
import com.bank.kyc.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repo;

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
			
			// ✅ Store blob
			customer.setAadharBlob(aadhar.getBytes());
			customer.setPanBlob(pan.getBytes());
			customer.setPhotoBlob(photo.getBytes());
			
			// ✅ Store metadata
			customer.setAadharOriginalName(aadhar.getOriginalFilename());
			customer.setPanOriginalName(pan.getOriginalFilename());
			customer.setPhotoOriginalName(photo.getOriginalFilename());
			
			customer.setAadharContentType(aadhar.getContentType());
			customer.setPanContentType(pan.getContentType());
			customer.setPhotoContentType(photo.getContentType());
			
			customer.setKycStatus("Pending");
			customer.setSubmittedAt(LocalDateTime.now());
			
			return repo.save(customer);
	}



    private void saveFile(MultipartFile file, String uploadDir, String label) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String extension = "";

        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        Path path = Paths.get(uploadDir + "/" + label + extension);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
    }

    public Customer verifyKyc(Long id) {
        return repo.findById(id)
            .map(c -> {
                c.setKycStatus("Verified");
                c.setVerifiedAt(LocalDateTime.now()); // ✅ Set only on verification
                return repo.save(c);
            })
            .orElseThrow(() -> new RuntimeException("❌ Customer not found"));
    }


    public List<Customer> getAll() {
        return repo.findAll();
    }

    public Customer getByEmail(String email) {
        String cleanEmail = email.trim(); // ✅ Removes newline, spaces
        return repo.findByUserEmail(cleanEmail)
            .orElseThrow(() -> new RuntimeException("No KYC found"));
    }


    public Customer getById(Long id) {
        return repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));
    }
    
    public Customer rejectKyc(Long id) {
        return repo.findById(id)
            .map(c -> {
                c.setKycStatus("Rejected");
                c.setVerifiedAt(null); // ✅ Explicitly clear timestamp
                return repo.save(c);
            })
            .orElseThrow(() -> new RuntimeException("❌ Customer not found"));
    }


}
