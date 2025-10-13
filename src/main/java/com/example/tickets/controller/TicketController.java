package com.example.tickets.controller;

import com.example.tickets.dto.TicketRequest;
import com.example.tickets.dto.TicketResponse;
import com.example.tickets.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects/{projectId}/tickets")
public class TicketController {

    private final TicketService svc;

    public TicketController(TicketService svc) {
        this.svc = svc;
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> list(@PathVariable Long projectId) {
        return ResponseEntity.ok(svc.list(projectId));
    }

    @PostMapping
    public ResponseEntity<TicketResponse> create(@PathVariable Long projectId, @Valid @RequestBody TicketRequest req) {
        return ResponseEntity.ok(svc.create(projectId, req));
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> get(@PathVariable Long projectId, @PathVariable Long ticketId) {
        return ResponseEntity.ok(svc.get(projectId, ticketId));
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> put(@PathVariable Long projectId, @PathVariable Long ticketId,
                                              @Valid @RequestBody TicketRequest req) {
        return ResponseEntity.ok(svc.update(projectId, ticketId, req));
    }

    @PatchMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> patch(@PathVariable Long projectId, @PathVariable Long ticketId,
                                                @RequestBody TicketRequest req) {
        return ResponseEntity.ok(svc.patch(projectId, ticketId, req));
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> delete(@PathVariable Long projectId, @PathVariable Long ticketId) {
        svc.delete(projectId, ticketId);
        return ResponseEntity.noContent().build();
    }
}
