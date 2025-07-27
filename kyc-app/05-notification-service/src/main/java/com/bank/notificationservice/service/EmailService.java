package com.bank.notificationservice.service;

import com.bank.notificationservice.dto.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(NotificationRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getToEmail());
        message.setSubject(request.getSubject());
        message.setText(request.getBody());
        mailSender.send(message);
    }
}
