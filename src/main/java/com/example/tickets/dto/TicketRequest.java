package com.example.tickets.dto;

import com.example.tickets.entity.TicketPriority;
import com.example.tickets.entity.TicketState;
import com.example.tickets.entity.TicketType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TicketRequest {
    @NotBlank
    @Size(min = 1, max = 160)
    private String title;
    private TicketType type;
    private TicketPriority priority;
    private TicketState state;
}
