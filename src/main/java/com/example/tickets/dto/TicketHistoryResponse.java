package com.example.tickets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketHistoryResponse {
    private String field;
    private String oldValue;
    private String newValue;
    private String changedAt;
    private String changedBy;
}
