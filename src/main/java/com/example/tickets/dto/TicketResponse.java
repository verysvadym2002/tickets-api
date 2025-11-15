package com.example.tickets.dto;

import com.example.tickets.entity.TicketPriority;
import com.example.tickets.entity.TicketState;
import com.example.tickets.entity.TicketType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TicketResponse {
    private Long id;
    private String title;
    private TicketType type;
    private TicketPriority priority;
    private TicketState state;
    private Long projectId;
    private Long assigneeId;
    private String assigneeUsername;
}
