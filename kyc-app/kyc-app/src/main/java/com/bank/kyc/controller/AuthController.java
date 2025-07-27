package com.bank.kyc.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.bank.kyc.model.User;
import com.bank.kyc.model.User.Role;
import com.bank.kyc.service.UserService;

import java.net.URI;
import java.util.Optional;
import com.bank.kyc.service.CustomerService;
import org.springframework.web.bind.annotation.RequestParam;
import com.bank.kyc.model.User; // if used explicitly

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private CustomerService service;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String email,
                                           @RequestParam String password,
                                           @RequestParam String role) {
        User user = new User();
        user.setEmail(email.trim().toLowerCase());
        user.setPassword(password);
        user.setRole(Role.valueOf(role.toUpperCase()));
        userService.register(user);
        return ResponseEntity.ok("✅ Registration successful.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email,
                                        @RequestParam String password,
                                        HttpSession session) {
        Optional<User> userOpt = userService.login(email.trim().toLowerCase(), password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            session.setAttribute("user", user);
            String redirectUrl = user.getRole() == Role.ADMIN ? "/verify.html" : "/index.html";
            return ResponseEntity.ok(redirectUrl);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Invalid credentials.");
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("✅ Logged out successfully.");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return (user != null)
            ? ResponseEntity.ok(user)
            : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Not logged in.");
    }

    @GetMapping("/")
    public ResponseEntity<Void> redirectRoot() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/register.html"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
    
    private void validateAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != Role.ADMIN) {
            throw new RuntimeException("❌ Only admins can perform this action.");
        }
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<?> rejectWithReason(@PathVariable Long id,
                                              @RequestParam String reason,
                                              HttpSession session) {
        validateAdmin(session);
        return ResponseEntity.ok(service.rejectKyc(id, reason));
    }

}
