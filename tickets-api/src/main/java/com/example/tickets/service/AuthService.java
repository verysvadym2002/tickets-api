package com.example.tickets.service;

import com.example.tickets.dto.*;
import com.example.tickets.entity.PasswordResetToken;
import com.example.tickets.entity.User;
import com.example.tickets.exception.NotFoundException;
import com.example.tickets.exception.UnauthorizedException;
import com.example.tickets.repository.PasswordResetTokenRepository;
import com.example.tickets.repository.UserRepository;
import com.example.tickets.security.JwtService;
import com.example.tickets.security.SecurityUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import com.example.tickets.entity.Role;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordResetTokenRepository resetRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepo,
                       PasswordResetTokenRepository resetRepo,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authManager,
                       JwtService jwtService) {
        this.userRepo = userRepo;
        this.resetRepo = resetRepo;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest request) {
        if (userRepo.existsByUsername(request.getUsername()))
            throw new UnauthorizedException("Username already exists");
        if (userRepo.existsByEmail(request.getEmail()))
            throw new UnauthorizedException("Email already exists");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        userRepo.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Invalid credentials");
        }
        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);

    }

    public void changePassword(ChangePasswordRequest req) {
        String username = SecurityUtils.currentUsername();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            throw new UnauthorizedException("Old password incorrect");
        }

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepo.save(user);
    }

    public String requestPasswordReset(PasswordResetRequest req) {
        Optional<User> optUser = userRepo.findByUsername(req.getLogin());
        if (optUser.isEmpty()) {
            optUser = userRepo.findByEmail(req.getLogin());
        }
        User user = optUser.orElseThrow(() -> new NotFoundException("User not found"));

        resetRepo.deleteByUser(user);

        PasswordResetToken token = new PasswordResetToken();
        token.setCode(UUID.randomUUID().toString());
        token.setExpiresAt(Instant.now().plusSeconds(600));
        token.setUser(user);
        resetRepo.save(token);

        return token.getCode();
    }

    public void resetPassword(PasswordResetConfirmRequest req) {
        PasswordResetToken token = resetRepo.findByCode(req.getCode())
                .orElseThrow(() -> new NotFoundException("Invalid reset code"));

        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new UnauthorizedException("Reset code expired");
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepo.save(user);
        resetRepo.delete(token);
    }

    public User getCurrentUser() {
        String username = SecurityUtils.currentUsername();
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
