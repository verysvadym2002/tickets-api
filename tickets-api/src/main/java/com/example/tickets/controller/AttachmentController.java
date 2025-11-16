package com.example.tickets.controller;

import com.example.tickets.dto.AttachmentRequest;
import com.example.tickets.dto.AttachmentResponse;
import com.example.tickets.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService service;

    // ------------------ PROJECT attachments ------------------

    @GetMapping("/projects/{projectId}/attachments")
    public List<AttachmentResponse> listProject(@PathVariable Long projectId) {
        return service.listForProject(projectId);
    }

    @PostMapping("/projects/{projectId}/attachments")
    public AttachmentResponse addProject(
            @PathVariable Long projectId,
            @RequestBody AttachmentRequest req
    ) {
        return service.addToProject(projectId, req);
    }

    // ------------------ TICKET attachments ------------------

    @GetMapping("/projects/{projectId}/tickets/{ticketId}/attachments")
    public List<AttachmentResponse> listTicket(@PathVariable Long ticketId) {
        return service.listForTicket(ticketId);
    }

    @PostMapping("/projects/{projectId}/tickets/{ticketId}/attachments")
    public AttachmentResponse addTicket(
            @PathVariable Long ticketId,
            @RequestBody AttachmentRequest req
    ) {
        return service.addToTicket(ticketId, req);
    }

    // ------------------ DELETE ------------------

    @DeleteMapping("/attachments/{attachmentId}")
    public void delete(@PathVariable Long attachmentId) {
        service.delete(attachmentId);
    }
}
