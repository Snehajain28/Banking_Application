package com.bank.customerservice.controller;

import com.bank.customerservice.model.Customer;
import com.bank.customerservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/{id}/email")
    public ResponseEntity<String> getCustomerEmail(@PathVariable Long id) {
        try {
            Customer customer = customerService.getCustomer(id);
            return ResponseEntity.ok(customer.getEmail());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
    }

    
    @PostMapping("/{id}/upload-{type}")
    public ResponseEntity<String> uploadDocument(
            @PathVariable Long id,
            @PathVariable String type,
            @RequestParam("file") MultipartFile file) {
        try {
            customerService.uploadDocument(id, file, type);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/download-{type}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id, @PathVariable String type) {
        try {
            byte[] fileBytes = customerService.downloadDocument(id, type);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + type + "_" + id + ".pdf")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/kyc-status")
    public ResponseEntity<String> updateKycStatus(@PathVariable Long id, @RequestParam("status") String status) {
        try {
            customerService.updateKycStatus(id, status);
            return ResponseEntity.ok("KYC status updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        try {
            Customer customer = customerService.getCustomer(id);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
