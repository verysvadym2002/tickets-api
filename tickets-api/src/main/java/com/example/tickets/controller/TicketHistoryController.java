package com.example.tickets.controller;

import com.example.tickets.dto.TicketHistoryResponse;
import com.example.tickets.service.TicketHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/tickets/{ticketId}/history")
public class TicketHistoryController {

    private final TicketHistoryService svc;

    @GetMapping
    public List<TicketHistoryResponse> getHistory(
            @PathVariable Long projectId,
            @PathVariable Long ticketId
    ) {
        return svc.getHistory(projectId, ticketId);
    }
}
