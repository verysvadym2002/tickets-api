package com.example.tickets.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PasswordResetConfirmRequest {
    @NotBlank
    private String code;
    @NotBlank
    private String newPassword;
}
