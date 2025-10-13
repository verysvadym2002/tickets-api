package com.example.tickets.service;

import com.example.tickets.dto.TicketRequest;
import com.example.tickets.dto.TicketResponse;
import com.example.tickets.entity.Project;
import com.example.tickets.entity.Ticket;
import com.example.tickets.entity.User;
import com.example.tickets.exception.NotFoundException;
import com.example.tickets.repository.ProjectRepository;
import com.example.tickets.repository.TicketRepository;
import com.example.tickets.repository.UserRepository;
import com.example.tickets.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    private User currentUser() {
        String username = SecurityUtils.currentUsername();
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Current user not found"));
    }

    private Project findProjectOwned(Long projectId) {
        User u = currentUser();
        return projectRepository.findByIdAndOwner(projectId, u)
                .orElseThrow(() -> new NotFoundException("Project not found or not accessible"));
    }

    public TicketResponse create(Long projectId, TicketRequest req) {
        Project project = findProjectOwned(projectId);
        Ticket t = new Ticket();
        t.setTitle(req.getTitle());
        if (req.getType() != null) t.setType(req.getType());
        if (req.getPriority() != null) t.setPriority(req.getPriority());
        if (req.getState() != null) t.setState(req.getState());
        t.setProject(project);
        Ticket saved = ticketRepository.save(t);
        return toDto(saved);
    }

    public List<TicketResponse> list(Long projectId) {
        Project project = findProjectOwned(projectId);
        return ticketRepository.findAllByProject(project).stream().map(this::toDto).collect(Collectors.toList());
    }

    public TicketResponse get(Long projectId, Long ticketId) {
        Project project = findProjectOwned(projectId);
        Ticket t = ticketRepository.findByIdAndProject(ticketId, project)
                .orElseThrow(() -> new NotFoundException("Ticket not found or not accessible"));
        return toDto(t);
    }

    public TicketResponse update(Long projectId, Long ticketId, TicketRequest req) {
        Project project = findProjectOwned(projectId);
        Ticket t = ticketRepository.findByIdAndProject(ticketId, project)
                .orElseThrow(() -> new NotFoundException("Ticket not found or not accessible"));
        t.setTitle(req.getTitle());
        if (req.getType() != null) t.setType(req.getType());
        if (req.getPriority() != null) t.setPriority(req.getPriority());
        if (req.getState() != null) t.setState(req.getState());
        return toDto(ticketRepository.save(t));
    }

    public TicketResponse patch(Long projectId, Long ticketId, TicketRequest req) {
        Project project = findProjectOwned(projectId);
        Ticket t = ticketRepository.findByIdAndProject(ticketId, project)
                .orElseThrow(() -> new NotFoundException("Ticket not found or not accessible"));

        if (req.getTitle() != null) t.setTitle(req.getTitle());
        if (req.getType() != null) t.setType(req.getType());
        if (req.getPriority() != null) t.setPriority(req.getPriority());
        if (req.getState() != null) t.setState(req.getState());

        return toDto(ticketRepository.save(t));
    }

    public void delete(Long projectId, Long ticketId) {
        Project project = findProjectOwned(projectId);
        Ticket t = ticketRepository.findByIdAndProject(ticketId, project)
                .orElseThrow(() -> new NotFoundException("Ticket not found or not accessible"));
        ticketRepository.delete(t);
    }

    private TicketResponse toDto(Ticket t) {
        return new TicketResponse(t.getId(), t.getTitle(), t.getType(), t.getPriority(), t.getState(), t.getProject().getId());
    }
}
