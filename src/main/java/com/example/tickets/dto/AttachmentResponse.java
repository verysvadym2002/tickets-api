package com.example.tickets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttachmentResponse {
    private Long id;
    private String url;
    private String uploadedBy;
    private String createdAt;
}
