package com.bank.kyc.controller;

import com.bank.kyc.model.*;
import com.bank.kyc.service.CustomerService;
import com.bank.kyc.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @Autowired
    private UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<KycResponse> uploadKyc(@RequestParam String name,
                                                 @RequestParam String email,
                                                 @RequestParam MultipartFile aadhar,
                                                 @RequestParam MultipartFile pan,
                                                 @RequestParam MultipartFile photo) {
        try {
            User registeredUser = userService.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("❌ User not found. Please register first."));

            Customer customer = new Customer();
            customer.setName(name);
            customer.setUser(registeredUser);

            Customer savedCustomer = service.saveKyc(customer, aadhar, pan, photo);

            return ResponseEntity.ok(new KycResponse("success", "✅ KYC submitted.", savedCustomer.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new KycResponse("error", "❌ Upload failed: " + e.getMessage(), null));
        }
    }

    @GetMapping("/admin/submissions")
    public ResponseEntity<?> getAllSubmissions(HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null || sessionUser.getRole() != User.Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("🚫 Access denied. Admin only.");
        }

        List<CustomerSummary> summaries = service.getAll().stream()
            .filter(c -> c.getUser() != null)
            .map(CustomerSummary::new)
            .toList();

        return ResponseEntity.ok(summaries);
    }

    @PutMapping("/verify/{id}")
    public ResponseEntity<?> verify(@PathVariable Long id, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null || sessionUser.getRole() != User.Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("🚫 Access denied.");
        }
        return ResponseEntity.ok(service.verifyKyc(id));
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<?> reject(@PathVariable Long id, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null || sessionUser.getRole() != User.Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("🚫 Access denied.");
        }
        return ResponseEntity.ok(service.rejectKyc(id));
    }

    @GetMapping("/status/{email}")
    public ResponseEntity<?> getStatusByEmail(@PathVariable String email, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null || !sessionUser.getEmail().equalsIgnoreCase(email.trim())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("🚫 Please log in to check your KYC status.");
        }

        CustomerStatus status = new CustomerStatus(service.getByEmail(email.trim()));
        return ResponseEntity.ok(status);
    }

    @GetMapping("/download/{id}/{type}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id,
                                               @PathVariable String type,
                                               HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Customer customer = service.getById(id);
        byte[] data;
        String originalName;
        String contentType;

        switch (type.toLowerCase()) {
            case "aadhaar":
                data = customer.getAadharBlob();
                originalName = customer.getAadharOriginalName();
                contentType = customer.getAadharContentType();
                break;
            case "pan":
                data = customer.getPanBlob();
                originalName = customer.getPanOriginalName();
                contentType = customer.getPanContentType();
                break;
            case "photo":
                data = customer.getPhotoBlob();
                originalName = customer.getPhotoOriginalName();
                contentType = customer.getPhotoContentType();
                break;
            default:
                throw new RuntimeException("Invalid file type");
        }

        if (data == null || data.length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(("❌ File not found or empty.").getBytes());
        }

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalName + "\"")
            .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
            .body(data);
    }



}
