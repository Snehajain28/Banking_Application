package com.bank.kyc.service;

import com.bank.kyc.model.User;
import com.bank.kyc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User register(User user) {
        Optional<User> existing = userRepo.findByEmail(user.getEmail());
        if (existing.isPresent()) {
            throw new RuntimeException("❌ This email is already registered.");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public Optional<User> login(String email, String rawPassword) {
        Optional<User> userOpt = userRepo.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (encoder.matches(rawPassword, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
