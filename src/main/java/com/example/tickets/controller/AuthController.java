package com.example.tickets.controller;

import com.example.tickets.dto.*;
import com.example.tickets.entity.User;
import com.example.tickets.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService svc;

    public AuthController(AuthService svc) {
        this.svc = svc;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest req) {
        svc.register(req);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(svc.login(req));
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest req) {
        svc.changePassword(req);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<String> requestReset(@Valid @RequestBody PasswordResetRequest req) {
        String code = svc.requestPasswordReset(req);
        return ResponseEntity.ok("Reset code generated (for testing): " + code);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetConfirmRequest req) {
        svc.resetPassword(req);
        return ResponseEntity.ok("Password reset successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<User> me() {
        return ResponseEntity.ok(svc.getCurrentUser());
    }
}
