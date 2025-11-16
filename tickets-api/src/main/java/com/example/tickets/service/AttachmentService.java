package com.example.tickets.service;

import com.example.tickets.dto.AttachmentRequest;
import com.example.tickets.dto.AttachmentResponse;
import com.example.tickets.entity.*;
import com.example.tickets.exception.NotFoundException;
import com.example.tickets.exception.UnauthorizedException;
import com.example.tickets.repository.*;
import com.example.tickets.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final TicketRepository ticketRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    // -----------------------------------------
    // Helper – get current user
    // -----------------------------------------
    private User currentUser() {
        String username = SecurityUtils.currentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Current user not found"));
    }

    // -----------------------------------------
    // Helper – check owner/admin
    // -----------------------------------------
    private void checkProjectAccess(Project project) {
        User user = currentUser();

        boolean isOwner = project.getOwner().getId().equals(user.getId());
        boolean isAdmin = user.getRoles().contains(Role.ROLE_ADMIN);

        if (!isOwner && !isAdmin) {
            throw new UnauthorizedException("You cannot edit this project");
        }
    }

    // -----------------------------------------
    // Ticket attachments
    // -----------------------------------------
    public List<AttachmentResponse> listForTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        return attachmentRepository.findByTicket(ticket)
                .stream()
                .map(a -> new AttachmentResponse(
                        a.getId(),
                        a.getUrl(),
                        a.getUploadedBy().getUsername(),
                        a.getCreatedAt().toString()
                ))
                .toList();
    }

    public AttachmentResponse addToTicket(Long ticketId, AttachmentRequest req) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        checkProjectAccess(ticket.getProject());

        User user = currentUser();

        Attachment a = new Attachment();
        a.setUrl(req.getUrl());
        a.setCreatedAt(Instant.now());
        a.setUploadedBy(user);
        a.setTicket(ticket);

        Attachment saved = attachmentRepository.save(a);

        return new AttachmentResponse(
                saved.getId(),
                saved.getUrl(),
                saved.getUploadedBy().getUsername(),
                saved.getCreatedAt().toString()
        );
    }

    // -----------------------------------------
    // Project attachments
    // -----------------------------------------
    public List<AttachmentResponse> listForProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        return attachmentRepository.findByProject(project)
                .stream()
                .map(a -> new AttachmentResponse(
                        a.getId(),
                        a.getUrl(),
                        a.getUploadedBy().getUsername(),
                        a.getCreatedAt().toString()
                ))
                .toList();
    }

    public AttachmentResponse addToProject(Long projectId, AttachmentRequest req) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        checkProjectAccess(project);

        User user = currentUser();

        Attachment a = new Attachment();
        a.setUrl(req.getUrl());
        a.setCreatedAt(Instant.now());
        a.setUploadedBy(user);
        a.setProject(project);

        Attachment saved = attachmentRepository.save(a);

        return new AttachmentResponse(
                saved.getId(),
                saved.getUrl(),
                saved.getUploadedBy().getUsername(),
                saved.getCreatedAt().toString()
        );
    }

    // -----------------------------------------
    // Delete attachment (secure)
    // -----------------------------------------
    public void delete(Long attachmentId) {
        Attachment a = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new NotFoundException("Attachment not found"));

        Project project = a.getProject() != null
                ? a.getProject()
                : a.getTicket().getProject();

        checkProjectAccess(project);

        attachmentRepository.delete(a);
    }
}
