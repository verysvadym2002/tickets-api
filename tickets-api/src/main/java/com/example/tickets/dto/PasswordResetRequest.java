package com.example.tickets.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PasswordResetRequest {
    @NotBlank
    private String login; // username or email
}
