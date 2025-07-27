package com.bank.kyc.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.bank.kyc.model.User;
import com.bank.kyc.model.User.Role;
import com.bank.kyc.service.UserService;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
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

   

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String email,
                                        @RequestParam String password,
                                        HttpSession session) {
  
    	Optional<User> userOpt = userService.login(email.trim().toLowerCase(), password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            session.setAttribute("user", user);

            Map<String, Object> response = new HashMap<>();
            response.put("email", user.getEmail());
            response.put("role", user.getRole());
           
            return ResponseEntity.ok(response);

        }
        Map<String, Object> body = new HashMap<>();
        body.put("message", "❌ Invalid credentials.");
        body.put("data", ""); // or any other field/value you want

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("✅ Logged out successfully.");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(HttpSession session) {
       System.out.println("idhks");
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
