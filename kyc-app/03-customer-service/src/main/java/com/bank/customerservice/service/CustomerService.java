package com.bank.customerservice.service;

import com.bank.customerservice.model.Customer;
import com.bank.customerservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer uploadDocument(Long id, MultipartFile file, String type) throws IOException {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isEmpty()) throw new RuntimeException("Customer not found");

        Customer customer = optionalCustomer.get();
        byte[] data = file.getBytes();

        switch (type.toLowerCase()) {
            case "aadhar":
                customer.setAadharBlob(data);
                customer.setAadharContentType(file.getContentType());
                customer.setAadharOriginalName(file.getOriginalFilename());
                break;
            case "pan":
                customer.setPanBlob(data);
                customer.setPanContentType(file.getContentType());
                customer.setPanOriginalName(file.getOriginalFilename());
                break;
            case "photo":
                customer.setPhotoBlob(data);
                customer.setPhotoContentType(file.getContentType());
                customer.setPhotoOriginalName(file.getOriginalFilename());
                break;
            default:
                throw new RuntimeException("Invalid document type");
        }

        return customerRepository.save(customer);
    }

    public byte[] downloadDocument(Long id, String type) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return switch (type.toLowerCase()) {
            case "aadhar" -> customer.getAadharBlob();
            case "pan" -> customer.getPanBlob();
            case "photo" -> customer.getPhotoBlob();
            default -> throw new RuntimeException("Invalid document type");
        };
    }

    public void updateKycStatus(Long id, String status) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setKycStatus(status);
        customerRepository.save(customer);
    }

    public Customer getCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }
}
