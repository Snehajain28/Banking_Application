package com.bank.kyc.controller;

import com.bank.kyc.model.*;
import com.bank.kyc.model.User.Role;
import com.bank.kyc.repository.AccountRepository;
import com.bank.kyc.service.CustomerService;
import com.bank.kyc.service.UserService;
import jakarta.servlet.http.HttpSession;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @Autowired
    private UserService userService;
    
    @Autowired
    private AccountRepository accountRepo;


//    @PostMapping("/upload")
//    public ResponseEntity<KycResponse> uploadKyc(@RequestParam String name,
//                                                 @RequestParam String email,
//                                                 @RequestParam MultipartFile aadhar,
//                                                 @RequestParam MultipartFile pan,
//                                                 @RequestParam MultipartFile photo) {
//        try {
//            User registeredUser = userService.findByEmail(email.trim().toLowerCase())
//                .orElseThrow(() -> new RuntimeException("❌ User not found. Please register first."));
//
//            Customer customer = new Customer();
//            customer.setName(name);
//            customer.setUser(registeredUser);
//
//            Customer savedCustomer = service.saveKyc(customer, aadhar, pan, photo);
//
//            return ResponseEntity.ok(new KycResponse("success", "✅ KYC submitted.", savedCustomer.getId()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(new KycResponse("error", "❌ Upload failed: " + e.getMessage(), null));
//        }
//    }
    @PostMapping("/upload")
    public ResponseEntity<KycResponse> uploadKyc(@RequestParam String name,
                                                 @RequestParam String email,
                                                 @RequestParam String dob,
                                                 @RequestParam String phone,
                                                 @RequestParam String address,
                                                 @RequestParam MultipartFile aadhar,
                                                 @RequestParam MultipartFile pan,
                                                 @RequestParam MultipartFile photo) {
        try {
            User registeredUser = userService.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("❌ User not found. Please register first."));

            Customer customer = new Customer();
            customer.setName(name);
            customer.setDob(dob);
            customer.setPhone(phone);
            customer.setAddress(address);
            customer.setUser(registeredUser);

            Customer savedCustomer = service.saveKyc(customer, aadhar, pan, photo);

            return ResponseEntity.ok(new KycResponse("success", "✅ KYC submitted.", savedCustomer.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new KycResponse("error", "❌ Upload failed: " + e.getMessage(), null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String email,
                                           @RequestParam String password,
                                           @RequestParam String name,
                                           @RequestParam String dob,
                                           @RequestParam String phone,
                                           @RequestParam String address,
                                           @RequestParam MultipartFile aadhar,
                                           @RequestParam MultipartFile pan,
                                           @RequestParam MultipartFile photo,
                                           HttpSession session ) {
        User user = new User();
        user.setEmail(email.trim().toLowerCase());
        user.setPassword(password);
        user.setRole(Role.valueOf("Customer".toUpperCase()));
        userService.register(user);
        uploadKyc(name, email, dob, phone, address, aadhar, pan, photo);
       
        session.setAttribute("user", user);
        return ResponseEntity.ok("✅ Registration successful.");
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
    public ResponseEntity<?> reject(@PathVariable Long id,
                                    @RequestParam String reason,
                                    HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null || sessionUser.getRole() != User.Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("🚫 Access denied.");
        }
        return ResponseEntity.ok(service.rejectKyc(id, reason));
    }


    @GetMapping("/status/{email}")
    public ResponseEntity<?> getStatusByEmail(@PathVariable String email,
                                              @RequestParam(required = false) boolean addCurrent,
                                              HttpSession session) {
   
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null || !sessionUser.getEmail().equalsIgnoreCase(email.trim())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("🚫 Please log in to check your KYC status.");
        }

        Customer customer = service.getByEmail(email.trim());
        if (addCurrent && !accountRepo.existsByCustomerIdAndAccountType(customer.getId(), "Current")) {
            Account current = new Account();
            current.setAccountType("Current");
            current.setCustomer(customer);
            accountRepo.save(current);
        }

        List<Account> accounts = accountRepo.findByCustomerId(customer.getId());
        CustomerStatus status = new CustomerStatus(customer);
        status.setAccounts(accounts.stream().map(Account::getAccountType).toList());

        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("accounts", accounts);
        response.put("sessionUser", sessionUser);
        response.put("email", customer.getUser().getEmail());
        response.put("role", customer.getUser().getRole());
       
        return ResponseEntity.ok(response);

 
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

    @PostMapping("/resubmit/{id}")
    public ResponseEntity<?> resubmit(@PathVariable Long id,
                                      @RequestParam MultipartFile aadhar,
                                      @RequestParam MultipartFile pan,
                                      @RequestParam MultipartFile photo,
                                      HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null || !sessionUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("🚫 Unauthorized.");
        }

        try {
            Customer updated = service.resubmitKyc(id, aadhar, pan, photo);
            return ResponseEntity.ok(new KycResponse("success", "✅ Documents resubmitted.", updated.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new KycResponse("error", "❌ Resubmission failed: " + e.getMessage(), null));
        }
    }


}
