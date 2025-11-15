package com.example.tickets.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AttachmentRequest {
    @NotBlank
    private String url;
}