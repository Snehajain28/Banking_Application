package com.bank.kyc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import com.bank.kyc.model.User; // if used explicitly

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void send(String to, String subject, String text) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(text);
            mailSender.send(msg);
            System.out.println("✅ Email sent to " + to + " | Subject: " + subject);
        } catch (Exception e) {
            System.out.println("❌ Failed to send email to " + to);
            e.printStackTrace();
        }
    }
}


