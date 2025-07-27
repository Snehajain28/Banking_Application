package com.bank.notificationservice.controller;

import com.bank.notificationservice.client.CustomerClient;
import com.bank.notificationservice.dto.NotificationRequest;
import com.bank.notificationservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomerClient customerClient;

    @PostMapping("/send/{customerId}")
    public ResponseEntity<String> sendKycStatusEmail(@PathVariable Long customerId) {
        String email = customerClient.getCustomerEmail(customerId);
        NotificationRequest request = new NotificationRequest();
        request.setToEmail(email);
        request.setSubject("KYC Status Update");
        request.setBody("Your KYC has been verified successfully.");

        emailService.sendEmail(request);
        return ResponseEntity.ok("Email sent to " + email);
    }
}
